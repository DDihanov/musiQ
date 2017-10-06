package com.dihanov.musiq.ui.main.main_fragments;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.models.ArtistTopTags;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultFragmentPresenter implements ArtistResultFragmentContract.Presenter {
    private static final long DELAY_IN_MILLIS = 500;
    private static final int limit = 20;
    private static final String LOADING_ARTIST = "hold on... i'm fetching this artist for you";
    private static final long ARTIST_LOADED_THREAD_TIMEOUT = 2000L;

    @Inject LastFmApiClient lastFmApiClient;

    private ArtistResultFragment artistResultFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private MainActivity mainActivity;

    @Inject
    public ArtistResultFragmentPresenter() {
    }

    @Override
    public void takeView(ArtistResultFragmentContract.View view) {
        this.artistResultFragment = (ArtistResultFragment) view;
        this.recyclerView = view.getRecyclerView();
        this.mainActivity = artistResultFragment.getMainActivity();
    }

    @Override
    public void leaveView() {
        if(this.compositeDisposable != null){
            compositeDisposable.clear();
        }
        this.artistResultFragment = null;
    }

    @Override
    public void addOnArtistResultClickedListener(ArtistAdapter.MyViewHolder viewHolder, String artistName) {
        RxView.clicks(viewHolder.thumbnail)
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    int pos = viewHolder.getLayoutPosition();
                    this.showArtistDetailsIntent(artistName);
                });
    }

    @Override
    public void addOnSearchBarTextChangedListener(MainActivity mainActivity, SearchView searchEditText) {
        ArtistResultFragmentPresenter artistResultFragmentPresenter = this;
        if(searchEditText == null){
            return;
        }
        Observable<ArtistSearchResults> autocompleteResponseObservable =
                RxSearchView.queryTextChanges(searchEditText)
                        .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                        .filter(s -> s.length() >= 2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(s -> {
                            Constants.checkConnection(mainActivity);
                            mainActivity.showProgressBar();
                        })
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<CharSequence, Observable<ArtistSearchResults>>() {
                            @Override
                            public Observable<ArtistSearchResults> apply(CharSequence s) throws Exception {
                                return lastFmApiClient.getLastFmApiService()
                                        .searchForArtist(s.toString(), limit);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry();


        autocompleteResponseObservable
//                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistSearchResults>() {
                    private static final String TAG = "ArtistResult";

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArtistSearchResults artistSearchResults) {
//                        mainActivity.showProgressBar();
                        Log.i(TAG, artistSearchResults.toString());

                        List<Artist> result = new ArrayList<>();
                        result.addAll(artistSearchResults.getResults().getArtistmatches().getArtistMatches());
                        if(result.isEmpty()){
                            result = Collections.emptyList();
                        }

                        ArtistAdapter artistAdapter = new ArtistAdapter(mainActivity, result, artistResultFragmentPresenter);

                        recyclerView.setAdapter(artistAdapter);
                        mainActivity.hideKeyboard();
                        mainActivity.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivity.hideProgressBar();
                        Log.e(TAG, "onError", e);
                        compositeDisposable.clear();
                    }
                });
    }

     private void showArtistDetailsIntent(String artistName) {
        Intent showArtistDetailsIntent = new Intent(mainActivity, ArtistDetailsActivity.class);
        showArtistDetailsIntent.putExtra(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        Constants.showTooltip(mainActivity, mainActivity.getBirdIcon(), LOADING_ARTIST);
        mainActivity.showProgressBar();

        Observable<ArtistTopTags> topArtistTags = lastFmApiClient.getLastFmApiService()
                .getArtistTopTags(artistName).subscribeOn(Schedulers.io());
        Observable<SpecificArtist> specificArtistRequest = lastFmApiClient.getLastFmApiService()
                .getSpecificArtistInfo(artistName)
                .subscribeOn(Schedulers.io());
        Observable<TopArtistAlbums> topAlbumRequest = lastFmApiClient.getLastFmApiService()
                .searchForArtistTopAlbums(artistName, Constants.ALBUM_LIMIT)
                .subscribeOn(Schedulers.newThread());

        Observable.zip(specificArtistRequest,
                topAlbumRequest,
                topArtistTags,
                new Function3<SpecificArtist, TopArtistAlbums, ArtistTopTags, HashMap<String, String>>() {
                    @Override
                    public HashMap<String, String> apply(SpecificArtist specificArtist, TopArtistAlbums topArtistAlbums, ArtistTopTags artistTopTags) throws Exception {
                        HashMap<String, String> result = new HashMap<>();

                        result.put(Constants.ARTIST, new Gson().toJson(specificArtist, SpecificArtist.class));
                        result.put(Constants.ALBUM, new Gson().toJson(topArtistAlbums, TopArtistAlbums.class));
                        result.put(Constants.TAGS, new Gson().toJson(artistTopTags, ArtistTopTags.class));

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
                        mainActivity.startActivity(showArtistDetailsIntent);
                    }
                });
    }
}
