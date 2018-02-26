package com.dihanov.musiq.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public abstract class ArtistDetailsIntentShowableImpl implements ArtistDetailsIntentShowable, SpecificArtistSearchable{
    private static final String LOADING_ARTIST = "hold on... i'm fetching this artist for you";

    @Inject LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void showArtistDetailsIntent(String artistName, MainContract.View mainActivity) {
        Intent showArtistDetailsIntent = new Intent((Context)mainActivity, ArtistDetails.class);
        showArtistDetailsIntent.putExtra(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        HelperMethods.showTooltip(mainActivity, mainActivity.getBirdIcon(), LOADING_ARTIST);
        mainActivity.showProgressBar();

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
                        for (Map.Entry<String, String> kvp : stringStringHashMap.entrySet()) {
                            showArtistDetailsIntent.putExtra(kvp.getKey(), kvp.getValue());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(ArtistAdapter.class.toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        //important we use .clear and not .dispose, since .dispose will not allow any further subscribing
                        compositeDisposable.clear();
                        mainActivity.hideProgressBar();
                        ((Activity)mainActivity).startActivity(showArtistDetailsIntent);
                    }
                });
    }
}
