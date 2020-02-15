package com.dihanov.musiq.service.scrobble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.db.ScrobbleDB;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.data.usecase.BulkScrobbleUseCase;
import com.dihanov.musiq.data.usecase.ScrobbleTrackUseCase;
import com.dihanov.musiq.data.usecase.UpdateNowPlayingUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Notificator;

import java.util.List;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class Scrobbler {
    private static long lastScrobbleTime = System.currentTimeMillis();
    private static final Object LOCK = new Object();
    private static final Object NOW_PLAYING_LOCK = new Object();
    private static final String TAG = Scrobbler.class.getSimpleName();
    private static State STATE;

    private ScrobbleDB scrobbleDB;
    private Scrobble nowPlaying;
    private Context context;
    private UserSettingsRepository userSettingsRepository;
    private Notificator notificator;
    private ScrobbleTrackUseCase scrobbleTrackUseCase;
    private BulkScrobbleUseCase bulkScrobbleUseCase;
    private UpdateNowPlayingUseCase updateNowPlayingUseCase;

    private UseCase.ResultCallback<Response> bulkScrobbleCallback = new UseCase.ResultCallback<Response>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(Response response) {
            AppLog.log(BulkScrobbleUseCase.class.getSimpleName(), "Scrobbled " +
                    response.getScrobbles().getScrobble().getArtist().getText() +
                    " - " +
                    response.getScrobbles().getScrobble().getTrack().getText());
        }

        @Override
        public void onError(Throwable e) {
            scrobbleDB.clearCache();
        }
    };

    private UseCase.ResultCallback<Response> updateNowPlayingCallback = new UseCase.ResultCallback<Response>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onSuccess(Response response) {
            AppLog.log(TAG, String.format("Updated now playing: %s - %s", nowPlaying.getArtistName(), nowPlaying.getTrackName()));
        }

        @Override
        public void onError(Throwable e) {

        }
    };

    public Scrobbler(UpdateNowPlayingUseCase updateNowPlayingUseCase, BulkScrobbleUseCase bulkScrobbleUseCase, ScrobbleTrackUseCase scrobbleTrackUseCase, Context context, ScrobbleDB scrobbleDB, UserSettingsRepository userSettingsRepository, Notificator notificator) {
        this.updateNowPlayingUseCase = updateNowPlayingUseCase;
        this.bulkScrobbleUseCase = bulkScrobbleUseCase;
        this.scrobbleTrackUseCase = scrobbleTrackUseCase;
        this.context = context;
        STATE = new State(State.NONE);
        this.scrobbleDB = scrobbleDB;
        this.notificator = notificator;
        this.userSettingsRepository = userSettingsRepository;
    }

    public void scrobble(Scrobble scrobble) {
        scrobbleTrackUseCase.invoke(new ScrobbleCallback(scrobble), scrobble);

    }

    private void resetPenalty() {
        if (STATE == null) {
            return;
        }
        getStatus().setPenalty(0);
        getStatus().setLastPauseTime(System.currentTimeMillis());
    }

    private void handleErrorResponse(Response response, Scrobble scrobble) {
        switch (response.getError()) {
            case Error.SERVICE_OFFLINE:
                storeInDb(scrobble);
                break;
            case Error.SERVICE_UNVAVAILABLE:
                storeInDb(scrobble);
                break;
            case Error.IVALID_SESSION_KEY:
                redirectToLogin();
                break;
            default:
                break;
        }
    }

    private void redirectToLogin() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(context, R.string.spired_session, Toast.LENGTH_SHORT).show();
        context.startActivity(intent);
    }

    public Scrobble getNowPlaying() {
        return this.nowPlaying;
    }

    private void setNowPlaying(Scrobble nowPlaying) {
        //check to see if user is logged in before continuing
        if (!userSettingsRepository.hasSessionKey()) return;

        if (nowPlaying == null) {
            this.nowPlaying = null;
            return;
        }

        if (userSettingsRepository.hasNotificationsEnabled() && userSettingsRepository.hasSessionKey()) {
            notificator.buildNotification(context, context.getString(R.string.now_scrobbling), String.format("%s - %s", nowPlaying.getArtistName(), nowPlaying.getTrackName()));
        }

        this.nowPlaying = nowPlaying;
        synchronized (NOW_PLAYING_LOCK) {
            updateNowPlayingUseCase.invoke(updateNowPlayingCallback, nowPlaying);
        }
    }

    public void setStatus(PlaybackState playbackState) {
        int state = playbackState.getState();

        synchronized (LOCK) {
            manageScrobble(nowPlaying);
        }

        if (playbackState.getState() == PlaybackState.STATE_NONE) {
            this.setNowPlaying(null);
        } else {
            Scrobbler.STATE.handleStatusChanged(state);
        }

    }

    public State getStatus() {
        return this.STATE;
    }

    private void storeInDb(Scrobble scrobble) {
        scrobbleDB.writeScrobble(scrobble);
    }

    public void scrobbleFromCache() {
        List<Scrobble> cached = scrobbleDB.getCachedScrobbles();

        if (cached.isEmpty() || userSettingsRepository.hasScrobbleReviewEnabled()) return;

        bulkScrobbleUseCase.invoke(bulkScrobbleCallback, cached);
    }

    public void scrobbleFromCacheDirectly() {
        List<Scrobble> cached = scrobbleDB.getCachedScrobbles();

        if (cached.isEmpty()) return;

        bulkScrobbleUseCase.invoke(bulkScrobbleCallback, cached);
    }

    public void updateTrackInfo(MediaMetadata metadata) {
        if (this.nowPlaying != null) {
            synchronized (LOCK) {
                manageScrobble(this.nowPlaying);
            }
        }

        if (metadata == null) {
            return;
        }

        resetPenalty();
        String trackName = metadata.getString(MediaMetadata.METADATA_KEY_TITLE);
        String trackArtistName = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST);
        String trackAlbumName = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM);
        String trackAlbumArtist = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST);
        Bitmap albumArt = metadata.getBitmap(MediaMetadata.METADATA_KEY_ART);
        long trackDuration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION);
        long timestamp = getUnixTime();

        if (albumArt == null) {
            albumArt = metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART);
        }

        if (trackName == null) {
            trackName = metadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE);

            if (trackName == null) {
                trackName = "";
            }
        }

        Scrobble scrobble = new Scrobble(trackArtistName, trackName, trackAlbumName, trackDuration, timestamp, albumArt);
        scrobble.setTrackStartTime(System.currentTimeMillis());
        AppLog.log(TAG, "updateTrackInfo: " + scrobble);

        if (!scrobble.isScrobbleValid()) return;

        this.setNowPlaying(scrobble);
    }

    private long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    private void manageScrobble(Scrobble scrobble) {
        if (scrobble == null) {
            return;
        }

        long currentTime = System.currentTimeMillis() - STATE.getPenalty();
        //multiply by 1000 to time in milliseconds
        long trackBeginTime = scrobble.getTrackStartTime();
        long minimumTrackTime = scrobble.getDuration() / 2;
        long requiredTime = trackBeginTime + minimumTrackTime;

        if (requiredTime <= currentTime) {
            long start = System.currentTimeMillis();
            if (lastScrobbleTime + 5000 >= start) {
                return;
            }
            if (userSettingsRepository.hasScrobbleReviewEnabled()) {
                storeInDb(scrobble);
            } else {
                this.scrobble(scrobble);
            }
            lastScrobbleTime = start;
        }
    }

    private class ScrobbleCallback implements UseCase.ResultCallback<Response> {
        private Scrobble scrobble;

        public ScrobbleCallback(Scrobble scrobble) {
            this.scrobble = scrobble;
        }

        @Override
        public void onStart() {
        }

        @Override
        public void onSuccess(Response response) {
            if (response != null) {
                if (response.getError() == null) {
                    AppLog.log(TAG, String.format("Scrobbled %s - %s", scrobble.getArtistName(), scrobble.getTrackName()));
                } else {
                    handleErrorResponse(response, scrobble);
                }
            }
            resetPenalty();
            if (nowPlaying != null) {
                nowPlaying.setTrackStartTime(System.currentTimeMillis());
            }

        }

        @Override
        public void onError(Throwable e) {
            storeInDb(scrobble);
            AppLog.log(TAG, e.getMessage());
        }
    }
}
