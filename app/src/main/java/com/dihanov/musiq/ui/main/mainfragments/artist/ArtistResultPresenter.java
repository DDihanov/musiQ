package com.dihanov.musiq.ui.main.mainfragments.artist;

import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
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
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultPresenter implements ArtistResultContract.Presenter{
    private static final int limit = 20;

    private final LastFmApiClient lastFmApiClient;

    private ArtistResultContract.View artistResultFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArtistResultPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistResultContract.View view) {
        this.artistResultFragment = view;
    }

    @Override
    public void leaveView() {
        if(this.compositeDisposable != null){
            compositeDisposable.clear();
        }
        this.artistResultFragment = null;
    }


    @Override
    public Observable<ArtistSearchResults> searchForArtist(String artistName) {
        return lastFmApiClient.getLastFmApiService()
                .searchForArtist(artistName, limit);
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
                        artistResultFragment.hideProgressBar();
                        artistResultFragment.startActivityWithExtras(bundleExtra);
                    }
                });
    }

    @Override
    public void publishResult(Observable<ArtistSearchResults> observable) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArtistSearchResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArtistSearchResults albumSearchResults) {
                        artistResultFragment.setSearchResults(albumSearchResults);
                        artistResultFragment.hideKeyboard();
                        artistResultFragment.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        artistResultFragment.hideProgressBar();
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        artistResultFragment.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }
}
