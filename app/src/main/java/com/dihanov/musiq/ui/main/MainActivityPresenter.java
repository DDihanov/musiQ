package com.dihanov.musiq.ui.main;


import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.service.LastFmApiClient;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    MainActivityContract.View mainActivityView;

    @Inject
    public MainActivityPresenter() {
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        this.mainActivityView = null;
    }

    @Override
    public void addDataToSearchList(String searchQuery, List<String> searchList) {
//        RxTextView
//                .textChanges(editText)
//                .filter(charSequence -> charSequence.length() >= 2)
//                .debounce(1000, TimeUnit.MILLISECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(ch -> {
//                    mainActivityView.hideProgressBar();
//                    addToResult(ch, searchList);
//                });

        Single.just(searchQuery)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
                .filter(charSequence -> charSequence.length() >= 2)
                .subscribe(ch -> {
                    addToResult(ch, searchList);
                });
    }

    @Override
    public void setSearchListener(AutoCompleteTextView v) {
        RxTextView.textChanges(v)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence query) throws Exception {
                        return query.length() >= 2;
                    }
                }).debounce(1000, TimeUnit.MILLISECONDS);
    }

    private void addToResult(CharSequence ch, List<String> searchList) {
//        lastFmApiClient
//                .getLastFmApiService()
//                .searchForArtist(ch.toString(), 10)
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnNext(artistSearchResults -> mainActivityView.showProgressBar())
//                .subscribeOn(Schedulers.io())
//                .subscribe(artistSearchResults ->
//                {
//                    for (Artist artist : artistSearchResults.getResults().getArtistmatches().getArtist()) {
//                        searchList.add(artist.getName());
//                    }
//                }, throwable -> Log.v("BIG ERROR", throwable.getMessage(), throwable))
//                .dispose();
        lastFmApiClient
                .getLastFmApiService()
                .searchForArtist(ch.toString(), 10)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArtistSearchResults>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        searchList.clear();
                        mainActivityView.showProgressBar();
                    }

                    @Override
                    public void onNext(ArtistSearchResults artistSearchResults) {
                        for (Artist artist : artistSearchResults.getResults().getArtistmatches().getArtist()) {
                            searchList.add(artist.getName());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("ERROR", e.getMessage(), e);
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgressBar();
                        mainActivityView.refreshSearchBar();
                    }
                });
    }
}
