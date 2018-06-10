package com.dihanov.musiq.ui.settings.profile.user_loved_tracks;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.UserLovedTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.TrackLoveManager;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserLovedTracksPresenter implements UserLovedTracksContract.Presenter {
    private final String TAG = getClass().getSimpleName();

    private LastFmApiClient lastFmApiClient;
    private UserLovedTracksContract.View view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private TrackLoveManager trackLoveManager;

    @Inject
    public UserLovedTracksPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void fetchLovedTracks(int limit) {
        lastFmApiClient.getLastFmApiService()
                .getUserLovedTracks(App.getSharedPreferences().getString(Constants.USERNAME, ""), limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserLovedTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        view.showProgressBar();
                    }

                    @Override
                    public void onNext(UserLovedTracks userLovedTracks) {
                        if (userLovedTracks == null ||
                                userLovedTracks.getLovedtracks() == null ||
                                userLovedTracks.getLovedtracks().getTrack() == null
                                || view == null) {
                            return;
                        }
                        view.loadLovedTracks(userLovedTracks.getLovedtracks().getTrack());
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                        AppLog.log(TAG, e.getMessage());
                        if (view != null){
                            view.hideProgressBar();
                        }
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        if (view != null){
                            view.hideProgressBar();
                        }
                    }
                });
    }

    @Override
    public void unloveTrack(String artistName, String trackName) {
        this.trackLoveManager.unloveTrack(artistName, trackName);
    }

    @Override
    public void takeView(UserLovedTracksContract.View view) {
        this.view = view;
        this.trackLoveManager = new TrackLoveManager(lastFmApiClient, view);
    }

    @Override
    public void leaveView() {
        this.view = null;
    }
}
