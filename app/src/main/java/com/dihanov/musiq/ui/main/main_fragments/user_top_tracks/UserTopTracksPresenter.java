package com.dihanov.musiq.ui.main.main_fragments.user_top_tracks;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.UserTopTracks;
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

public class UserTopTracksPresenter implements UserTopTracksContract.Presenter {
    private static final int LIMIT = 10;

    private final LastFmApiClient lastFmApiClient;

    private UserTopTracksContract.View userTopTracksFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public UserTopTracksPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(UserTopTracksContract.View view) {
        this.userTopTracksFragment = view;
    }


    @Override
    public void leaveView() {
        if (this.compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.userTopTracksFragment = null;
    }

    @Override
    public void loadTopTracks(String timeframe) {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        if (username.isEmpty() || username.equals("")) return;

        lastFmApiClient.getLastFmApiService()
                .getUserTopTracks(username, LIMIT, timeframe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTopTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        userTopTracksFragment.showProgressBar();
                    }

                    @Override
                    public void
                    onNext(UserTopTracks userTopTracks) {
                        userTopTracksFragment.configureBarChart(userTopTracks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(UserTopTracksPresenter.class.getSimpleName(), e.getMessage());
                        compositeDisposable.clear();
                        userTopTracksFragment.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        userTopTracksFragment.hideProgressBar();
                    }
                });
    }
}
