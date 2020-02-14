package com.dihanov.musiq.ui.main.main_fragments.user_top_artists;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopArtistsPresenter implements UserTopArtistsContract.Presenter {
    private static final int LIMIT = 10;


    private final LastFmApiClient lastFmApiClient;

    private UserTopArtistsContract.View userTopTracks;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public UserTopArtistsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(UserTopArtistsContract.View view) {
        this.userTopTracks = view;
    }


    @Override
    public void leaveView() {
        if (this.compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.userTopTracks = null;
    }

    @Override
    public void loadTopArtists(String timeframe) {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        if (username.isEmpty() || username.equals("")) return;

        lastFmApiClient.getLastFmApiService()
                .getUserTopArtists(username, LIMIT, timeframe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTopArtists>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        userTopTracks.showProgressBar();
                    }

                    @Override
                    public void onNext(UserTopArtists userTopArtistsWrapper) {
                        userTopTracks.configureBarChart(userTopArtistsWrapper);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(UserTopArtistsPresenter.class.getSimpleName(), e.getMessage());
                        compositeDisposable.clear();
                        userTopTracks.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        userTopTracks.hideProgressBar();
                    }
                });
    }
}
