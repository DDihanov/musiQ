package com.dihanov.musiq.ui.main;

import com.dihanov.musiq.db.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    private MainContract.View mainActivityView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final LastFmApiClient lastFmApiClient;
    private UserSettingsRepository userSettingsRepository;

    @Inject
    public MainPresenter(LastFmApiClient lastFmApiClient, UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(MainContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        compositeDisposable.clear();
        this.mainActivityView = null;
    }


    @Override
    public void getUserInfo() {
        String username = userSettingsRepository.getUsername();
        lastFmApiClient.getLastFmApiService()
                .getUserInfo(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        mainActivityView.showProgressBar();
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if ( userInfo == null || userInfo.getUser() == null ||
                                userInfo.getUser().getImage().get(Constants.IMAGE_LARGE) == null ||
                                userInfo.getUser().getPlaycount() == null) {
                            return;
                        }

                        String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
                        String playcount = userInfo.getUser().getPlaycount();

                        setUserInfo(profilePicUrl, playcount, username);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivityView.hideProgressBar();
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        mainActivityView.hideProgressBar();
                    }
                });
    }

    private void setUserInfo(String profilePicUrl, String playcount, String username) {
        userSettingsRepository.persistProfilePictureUrl(profilePicUrl);

        mainActivityView.setUserInfo(profilePicUrl, playcount, username);
    }

    @Override
    public void loadUserTopArtists(String timeframe, String username, int limit) {
        lastFmApiClient.getLastFmApiService()
                .getUserTopArtists(username, limit, timeframe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTopArtists>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainActivityView.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(UserTopArtists userTopArtists) {
                        if (userTopArtists == null || userTopArtists.getTopartists() == null || userTopArtists.getTopartists().getArtist() == null) {
                            return;
                        }
                        mainActivityView.setTopArtists(userTopArtists.getTopartists().getArtist());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivityView.hideProgressBar();
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void loadChartTopArtists(int limit) {
        lastFmApiClient.getLastFmApiService().chartTopArtists(limit)
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainActivityView.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        mainActivityView.setTopArtists(artists);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                        mainActivityView.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void fetchArtist(String artistName) {
        HashMap<String, String> bundleExtra = new HashMap<>();

        Observable<SpecificArtist> specificArtistRequest = lastFmApiClient.getLastFmApiService()
                .getSpecificArtistInfo(artistName)
                .subscribeOn(Schedulers.io());
        Observable<TopArtistAlbums> topAlbumRequest = lastFmApiClient.getLastFmApiService()
                .searchForArtistTopAlbums(artistName, Constants.ALBUM_LIMIT)
                .subscribeOn(Schedulers.newThread());

        Observable.zip(
                specificArtistRequest,
                topAlbumRequest,
                new BiFunction<SpecificArtist, TopArtistAlbums, HashMap<String, String>>() {
                    @Override
                    public HashMap<String, String> apply(SpecificArtist specificArtist, TopArtistAlbums topArtistAlbums) throws Exception {
                        HashMap<String, String> result = new HashMap<>();

                        result.put(Constants.ARTIST, new Gson().toJson(specificArtist, SpecificArtist.class));
                        result.put(Constants.ALBUM, new Gson().toJson(topArtistAlbums, TopArtistAlbums.class));

                        return result;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HashMap<String, String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(HashMap<String, String> stringStringHashMap) {
                        bundleExtra.putAll(stringStringHashMap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(ArtistAdapter.class.toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        //important we use .clear and not .dispose, since .dispose will not allow any further subscribing
                        compositeDisposable.clear();
                        mainActivityView.hideProgressBar();
                        mainActivityView.startActivityWithExtras(bundleExtra);
                    }
                });
    }
}
