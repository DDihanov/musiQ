package com.dihanov.musiq.service.scrobble;

import android.content.Context;

import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.service.LastFmApiClient;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class Scrobbler {
    private static final String TAG = Scrobbler.class.getSimpleName();
    //TODO:this will be the DBFlow impl
    //private ScrobbleDB scrobbleDB;
    private Scrobble nowPlaying;
    private Status status;
    private LastFmApiClient lastFmApiClient;
    private Context context;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public Scrobbler(LastFmApiClient lastFmApiClient, Context context){
        this.lastFmApiClient = lastFmApiClient;
        this.context = context;
    }

    public void scrobble(Scrobble scrobble){
//        String apiSig = Constants.generateSig("artist", "Mogwai",
//                "track", "Auto Rock",
//                "timestamp", "1518013498",
//                "method", Constants.TRACK_SCROBBLE_METHOD);
//        lastFmApiClient.getLastFmApiService().scrobbleTrack(Constants.TRACK_SCROBBLE_METHOD,
//                "Mogwai",
//                "Auto Rock",
//                Config.API_KEY,
//                apiSig,
//                1518013498,
//                App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
//                Config.FORMAT)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Observer<Response>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        compositeDisposable.add(d);
//                    }
//
//                    @Override
//                    public void onNext(Response response) {
//                        if(response.getError() == null){
//                            Toast.makeText(context, String.format(context.getString(R.string.scrobble_single_track_success),
//                                    response.getScrobbles().getScrobble().getArtist().getName(),
//                                    response.getScrobbles().getScrobble().getArtist().getName()), Toast.LENGTH_SHORT).show();
//                        } else {
//                            handleErrorResponse(response, scrobble);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Log.d(TAG, e.getMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        compositeDisposable.clear();
//                    }
//                });
    }

    private void handleErrorResponse(Response response, Scrobble scrobble) {
        switch(response.getError()){
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

    public Scrobble getNowPlaying(){
        return this.nowPlaying;
    }

    public void setNowPlaying(Scrobble nowPlaying) {
        this.nowPlaying = nowPlaying;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus(){
        return this.status;
    }

    private void storeInDb(Scrobble scrobble){
        //TODO
    }

    private void scrobbleFromCache(){
        //TODO
    }
}
