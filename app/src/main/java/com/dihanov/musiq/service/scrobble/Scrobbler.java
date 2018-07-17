package com.dihanov.musiq.service.scrobble;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.session.PlaybackState;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.db.ScrobbleDB;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.dihanov.musiq.util.Notificator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class Scrobbler {
    private static long lastScrobbleTime = System.currentTimeMillis();
    private static Object object = new Object();

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
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, scrobble.getArtistName(),
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
                        if(response != null){
                            if (response.getError() == null) {
                                AppLog.log(TAG, String.format("Scrobbled %s - %s", scrobble.getArtistName(), scrobble.getTrackName()));
                            } else {
                                handleErrorResponse(response, scrobble);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        storeInDb(scrobble);
                        AppLog.log(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        resetPenalty();
                        if(nowPlaying != null){
                            nowPlaying.setTrackStartTime(System.currentTimeMillis());
                        }
                        compositeDisposable.clear();
                    }
                });

    }

    private void resetPenalty() {
        if(state == null){
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
        App.getAppContext().startActivity(intent);
    }

    public Scrobble getNowPlaying() {
        return this.nowPlaying;
    }

    private void setNowPlaying(Scrobble nowPlaying) {
        //check to see if user is logged in before continuing
        if (!App.getSharedPreferences().contains(Constants.USER_SESSION_KEY)) return;

        if (nowPlaying == null) {
            this.nowPlaying = null;
            return;
        }

        if (App.getSharedPreferences().getBoolean("scrobble_notifications", true) && App.getSharedPreferences().contains(Constants.USER_SESSION_KEY)) {
            Notificator.buildNotification(context, context.getString(R.string.now_scrobbling), String.format("%s - %s", nowPlaying.getArtistName(), nowPlaying.getTrackName()));
        }

        this.nowPlaying = nowPlaying;
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, nowPlaying.getArtistName(),
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
                        AppLog.log(TAG, String.format("Updated now playing: %s - %s", nowPlaying.getArtistName(), nowPlaying.getTrackName()));
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    public void setStatus(PlaybackState playbackState) {
        int state = playbackState.getState();

        synchronized (object) {
            manageScrobble(nowPlaying);
        }

        if (playbackState.getState() == PlaybackState.STATE_NONE) {
            this.setNowPlaying(null);
        } else {
            Scrobbler.state.handleStatusChanged(state);
        }

    }

    public State getStatus() {
        return this.state;
    }

    private void storeInDb(Scrobble scrobble) {
        scrobbleDB.writeScrobble(scrobble);
    }

    public void scrobbleFromCache() {
        List<Scrobble> cached = scrobbleDB.getCachedScrobbles();

        if (cached.isEmpty() || App.getSharedPreferences().getBoolean("scrobble_review", false)) return;

        List<Observable<Response>> observables = new ArrayList<>();
        String apiSig = "";

        for (int i = 0; i < 50 && i < cached.size(); i++) {
            Scrobble scrobble = cached.get(i);
            String artistName = scrobble.getArtistName();
            String trackName = scrobble.getTrackName();
            String timestamp = String.valueOf(scrobble.getTimestamp());
            apiSig = HelperMethods.generateSig(Constants.ARTIST, artistName,
                    Constants.TRACK, trackName,
                    Constants.TIMESTAMP, timestamp,
                    Constants.METHOD, Constants.TRACK_SCROBBLE_METHOD);


            observables.add(lastFmApiClient.getLastFmApiService().scrobbleTrack(Constants.TRACK_SCROBBLE_METHOD,
                    artistName,
                    trackName,
                    Config.API_KEY,
                    apiSig,
                    timestamp,
                    App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                    Config.FORMAT)
                    .subscribeOn(Schedulers.io()));
        }

        Observable.zipIterable(observables, objects -> {
            List<Response> result = new ArrayList<>();
            for (Object object : objects) {
                result.add((Response) object);
            }
            return result;
        }, false, 10)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Response>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Response> responses) {
                        if (responses != null) {
                            for (Response respons : responses) {
                                AppLog.log(TAG, "Scrobbled " +
                                        respons.getScrobbles().getScrobble().getArtist().getText() +
                                        " - " +
                                        respons.getScrobbles().getScrobble().getTrack().getText());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        scrobbleDB.clearCache();
                    }
                });
    }

    public void scrobbleFromCacheDirectly() {
        List<Scrobble> cached = scrobbleDB.getCachedScrobbles();

        if (cached.isEmpty()) return;

        List<Observable<Response>> observables = new ArrayList<>();
        String apiSig = "";

        for (int i = 0; i < 50 && i < cached.size(); i++) {
            Scrobble scrobble = cached.get(i);
            String artistName = scrobble.getArtistName();
            String trackName = scrobble.getTrackName();
            String timestamp = String.valueOf(scrobble.getTimestamp());
            apiSig = HelperMethods.generateSig(Constants.ARTIST, artistName,
                    Constants.TRACK, trackName,
                    Constants.TIMESTAMP, timestamp,
                    Constants.METHOD, Constants.TRACK_SCROBBLE_METHOD);


            observables.add(lastFmApiClient.getLastFmApiService().scrobbleTrack(Constants.TRACK_SCROBBLE_METHOD,
                    artistName,
                    trackName,
                    Config.API_KEY,
                    apiSig,
                    timestamp,
                    App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                    Config.FORMAT)
                    .subscribeOn(Schedulers.io()));
        }

        Observable.zipIterable(observables, objects -> {
            List<Response> result = new ArrayList<>();
            for (Object object : objects) {
                result.add((Response) object);
            }
            return result;
        }, false, 10)
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Response>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Response> responses) {
                        if (responses != null) {
                            for (Response respons : responses) {
                                AppLog.log(TAG, "Scrobbled " +
                                        respons.getScrobbles().getScrobble().getArtist().getText() +
                                        " - " +
                                        respons.getScrobbles().getScrobble().getTrack().getText());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        scrobbleDB.clearCache();
                    }
                });
    }

    public void updateTrackInfo(MediaMetadata metadata) {
        if (this.nowPlaying != null) {
            synchronized (object) {
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

        long currentTime = System.currentTimeMillis() - state.getPenalty();
        //multiply by 1000 to time in milliseconds
        long trackBeginTime = scrobble.getTrackStartTime();
        long minimumTrackTime = scrobble.getDuration() / 2;
        long requiredTime = trackBeginTime + minimumTrackTime;

        if (requiredTime <= currentTime ) {
            long start = System.currentTimeMillis();
            if (lastScrobbleTime + 5000 >= start) {
                return;
            }
            if (App.getSharedPreferences().getBoolean("scrobble_review", false)) {
                storeInDb(scrobble);
            } else {
                this.scrobble(scrobble);
            }
            lastScrobbleTime = start;
        }
    }
}
