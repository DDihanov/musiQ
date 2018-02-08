package com.dihanov.musiq.service.scrobble;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.util.Log;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.db.ScrobbleDB;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.Constants;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class Scrobbler {
    private static final String TAG = Scrobbler.class.getSimpleName();
    private static State state;
    private ScrobbleDB scrobbleDB;
    private Scrobble nowPlaying;
    private LastFmApiClient lastFmApiClient;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Scrobbler(LastFmApiClient lastFmApiClient, Context context, ScrobbleDB scrobbleDB) {
        this.lastFmApiClient = lastFmApiClient;
        this.context = context;
        this.state = new State(State.NONE);
        this.scrobbleDB = scrobbleDB;
    }

    public void scrobble(Scrobble scrobble) {
        String apiSig = Constants.generateSig(Constants.ARTIST, scrobble.getArtistName(),
                Constants.TRACK, scrobble.getTrackName(),
                Constants.TIMESTAMP, String.valueOf(scrobble.getTimestamp()),
                Constants.METHOD, Constants.TRACK_SCROBBLE_METHOD);

        lastFmApiClient.getLastFmApiService().scrobbleTrack(Constants.TRACK_SCROBBLE_METHOD,
                scrobble.getArtistName(),
                scrobble.getTrackName(),
                Config.API_KEY,
                apiSig,
                scrobble.getTimestamp(),
                App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                Config.FORMAT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response response) {
                        if(response.getError() == null){
                            Toast.makeText(context, String.format(context.getString(R.string.scrobble_single_track_success),
                                    scrobble.getArtistName(),
                                    scrobble.getAlbumName()), Toast.LENGTH_SHORT).show();
                        } else {
                            handleErrorResponse(response, scrobble);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        storeInDb(scrobble);
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });

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
                //TODO
                break;
            default:
                break;
        }
    }

    public Scrobble getNowPlaying() {
        return this.nowPlaying;
    }

    public void setNowPlaying(Scrobble nowPlaying) {
        this.nowPlaying = nowPlaying;
        String apiSig = Constants.generateSig(Constants.ARTIST, nowPlaying.getArtistName(),
                Constants.TRACK, nowPlaying.getTrackName(),
                Constants.METHOD, Constants.UPDATE_NOW_PLAYING_METHOD);

        lastFmApiClient.getLastFmApiService()
                .updateNowPlaying(Constants.UPDATE_NOW_PLAYING_METHOD,
                        nowPlaying.getArtistName(),
                        nowPlaying.getTrackName(),
                        Config.API_KEY,
                        apiSig,
                        App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                        Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response response) {
                        Log.d(TAG, response.getMessage());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    public void setStatus(PlaybackState playbackState) {
        int state = playbackState.getState();
        this.state.handleStatusChanged(state);
    }


    public State getStatus() {
        return this.state;
    }

    private void storeInDb(Scrobble scrobble) {
        scrobbleDB.writeScrobble(scrobble);
    }

    private void scrobbleFromCache() {
        //TODO
    }

    public void updateTrackInfo(MediaMetadata metadata) {
        if (this.nowPlaying != null) {
            this.getStatus().setState(State.PREP_SCROBBLE_STATE);
            manageScrobble(this.nowPlaying);
        }

        this.getStatus().setPenalty(0);
        this.getStatus().setLastPauseTime(0);
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
        this.setNowPlaying(scrobble);
    }

    private long getUnixTime() {
        return System.currentTimeMillis() / 1000L;
    }

    private void manageScrobble(Scrobble scrobble) {
        long currentTime = System.currentTimeMillis() - state.getPenalty();
        //multiply by 1000 to time in milliseconds
        long trackBeginTime = scrobble.getTimestamp() * 1000L;
        long minimumTrackTime = scrobble.getDuration() / 2;
        long requiredTime = trackBeginTime + minimumTrackTime;

        if (currentTime >= requiredTime) {
            this.scrobble(scrobble);
        }
    }
}
