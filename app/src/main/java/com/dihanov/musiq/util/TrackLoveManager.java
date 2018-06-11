package com.dihanov.musiq.util;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.ToastShowable;
import com.dihanov.musiq.interfaces.TrackLovable;
import com.dihanov.musiq.interfaces.TrackUnlovable;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.service.LastFmApiClient;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TrackLoveManager implements TrackLovable, TrackUnlovable {
    private final String TAG = getClass().getSimpleName();

    private LastFmApiClient lastFmApiClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ToastShowable toastShowable;

    public TrackLoveManager(LastFmApiClient lastFmApiClient, ToastShowable toastShowable) {
        this.lastFmApiClient = lastFmApiClient;
        this.toastShowable = toastShowable;
    }


    @Override
    public void loveTrack(String artistName, String trackName) {
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, artistName,
                Constants.TRACK, trackName,
                Constants.METHOD, Constants.LOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .loveTrack(Constants.LOVE_TRACK_METHOD,
                        artistName,
                        trackName,
                        Config.API_KEY,
                        apiSig,
                        App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                        Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response response) {
                        if(response != null){
                            toastShowable.showToast(App.getAppContext(), App.getAppContext().getString(R.string.track_loved));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                        AppLog.log(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void unloveTrack(String artistName, String trackName) {
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, artistName,
                Constants.TRACK, trackName,
                Constants.METHOD, Constants.UNLOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .unloveTrack(Constants.UNLOVE_TRACK_METHOD,
                        artistName,
                        trackName,
                        Config.API_KEY,
                        apiSig,
                        App.getSharedPreferences().getString(Constants.USER_SESSION_KEY, ""),
                        Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Response response) {
                        if (response != null) {
                            toastShowable.showToast(App.getAppContext(), App.getAppContext().getString(R.string.track_unlove));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                        AppLog.log(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }
}
