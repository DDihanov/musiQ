package com.dihanov.musiq.ui.settings.profile.user_loved_tracks;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.UserLovedTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

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
                        view.loadLovedTracks(userLovedTracks.getLovedtracks().getTrack());
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                        AppLog.log(TAG, e.getMessage());
                        view.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        view.hideProgressBar();
                    }
                });
    }

    @Override
    public void unloveTrack(String artistName, String trackName) {
        String apiSig = HelperMethods.generateSig(Constants.ARTIST, artistName,
                Constants.TRACK, trackName,
                Constants.METHOD, Constants.UNLOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .loveTrack(Constants.UNLOVE_TRACK_METHOD,
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
                            view.showToast(App.getAppContext(), App.getAppContext().getString(R.string.track_loved));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        compositeDisposable.clear();
                        AppLog.log(view.getClass().getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void takeView(UserLovedTracksContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }
}
