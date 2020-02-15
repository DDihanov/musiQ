package com.dihanov.musiq.ui.settings.profile.userlovedtracks;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.db.UserSettingsRepository;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.UserLovedTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

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
    private UserSettingsRepository userSettingsRepository;
    private SigGenerator sigGenerator;

    @Inject
    public UserLovedTracksPresenter(LastFmApiClient lastFmApiClient, UserSettingsRepository userSettingsRepository, SigGenerator sigGenerator) {
        this.lastFmApiClient = lastFmApiClient;
        this.sigGenerator = sigGenerator;
        this.userSettingsRepository = userSettingsRepository;
    }

    @Override
    public void fetchLovedTracks(int limit) {
        lastFmApiClient.getLastFmApiService()
                .getUserLovedTracks(userSettingsRepository.getUsername(), limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserLovedTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        if (view != null){
                            view.showProgressBar();
                        }
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
    public void loveTrack(String artistName, String trackName) {
        String apiSig = sigGenerator.generateSig(Constants.ARTIST, artistName,
                Constants.TRACK, trackName,
                Constants.METHOD, Constants.LOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .loveTrack(Constants.LOVE_TRACK_METHOD,
                        artistName,
                        trackName,
                        Config.API_KEY,
                        apiSig,
                        userSettingsRepository.getUserSessionKey(),
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
                            view.showToastTrackLoved();
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
        String apiSig = sigGenerator.generateSig(Constants.ARTIST, artistName,
                Constants.TRACK, trackName,
                Constants.METHOD, Constants.UNLOVE_TRACK_METHOD);

        lastFmApiClient.getLastFmApiService()
                .unloveTrack(Constants.UNLOVE_TRACK_METHOD,
                        artistName,
                        trackName,
                        Config.API_KEY,
                        apiSig,
                        userSettingsRepository.getUserSessionKey(),
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
                            view.showToastTrackUnloved();
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
    public void takeView(UserLovedTracksContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }
}
