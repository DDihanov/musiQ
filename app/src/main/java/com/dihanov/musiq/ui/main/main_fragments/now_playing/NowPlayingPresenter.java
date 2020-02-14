package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.models.Response;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingPresenter implements NowPlayingContract.Presenter {
    private static final int RECENT_SCROBBLES_LIMIT = 20;
    private static final String TAG = NowPlayingPresenter.class.getSimpleName();

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private NowPlayingContract.View nowPlayingFragment;

    @Inject
    public NowPlayingPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(NowPlayingContract.View view) {
        this.nowPlayingFragment = view;
    }

    @Override
    public void leaveView() {
        this.nowPlayingFragment = null;
    }

    @Override
    public void loadRecentScrobbles() {
        lastFmApiClient.getLastFmApiService()
                .getUserRecentTracks(App.getSharedPreferences().getString(Constants.USERNAME, ""), RECENT_SCROBBLES_LIMIT, 1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecentTracksWrapper>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(RecentTracksWrapper recentTracksWrapper) {
                        if (nowPlayingFragment != null) {
                            nowPlayingFragment.setRecentTracks(recentTracksWrapper);
                        }
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
                            nowPlayingFragment.showToastTrackLoved();
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
                            nowPlayingFragment.showToastTrackUnloved();
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
                        nowPlayingFragment.hideProgressBar();
                        nowPlayingFragment.startActivityWithExtras(bundleExtra);
                        //important we use .clear and not .dispose, since .dispose will not allow any further subscribing
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
        lastFmApiClient.getLastFmApiService()
                .searchForSpecificAlbum(artistName, albumName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(specificAlbum -> specificAlbum)
                .subscribe(new Observer<SpecificAlbum>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        nowPlayingFragment.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SpecificAlbum specificAlbum) {
                        Album fullAlbum = specificAlbum.getAlbum();
                        nowPlayingFragment.showAlbumDetails(fullAlbum);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        nowPlayingFragment.hideProgressBar();
                    }
                });
    }
}


