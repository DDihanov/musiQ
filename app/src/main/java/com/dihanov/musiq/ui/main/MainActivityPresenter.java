package com.dihanov.musiq.ui.main;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.Connectivity;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private static final long DELAY_IN_MILLIS = 500;
    private final String NO_NETWORK_CONN_FOUND = "No network connection found.";

    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    MainActivityContract.View mainActivityView;

    private Disposable disposable;

    private Context context;

    @Inject
    public MainActivityPresenter(MainActivity mainActivity) {
        this.context = mainActivity;
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        if(this.disposable != null){
            disposable.dispose();
        }
        this.mainActivityView = null;
    }

    @Override
    public void addOnTextViewTextChangedObserver(final RecyclerView recyclerView, EditText searchEditText) {
        Observable<ArtistSearchResults> autocompleteResponseObservable =
                RxTextView.textChangeEvents(searchEditText)
                        .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                        .map(new Function<TextViewTextChangeEvent, String>() {
                            @Override
                            public String apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                                return textViewTextChangeEvent.text().toString();
                            }
                        })
                        .filter(s -> s.length() >= 2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(s -> {
                            if(!Connectivity.isConnected(context)){
                                makeToastInternetConn();
                            }
                            mainActivityView.showProgressBar();
                        })
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<String, Observable<ArtistSearchResults>>() {
                            @Override
                            public Observable<ArtistSearchResults> apply(String s) throws Exception {
                                return lastFmApiClient.getLastFmApiService()
                                        .searchForArtist(s, 10);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry();

        autocompleteResponseObservable
                .subscribe(new Observer<ArtistSearchResults>() {
                    private static final String TAG = "ArtistResult";

                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(ArtistSearchResults artistSearchResults) {
//                        mainActivityView.showProgressBar();
                        Log.i(TAG, artistSearchResults.toString());

                        List<Artist> result = new ArrayList<>();
                        result.addAll(artistSearchResults.getResults().getArtistmatches().getArtist());
                        if(result.isEmpty()){
                            result = Collections.emptyList();
                        }

                        ArtistAdapter artistAdapter = new ArtistAdapter(context, result);

                        recyclerView.setAdapter(artistAdapter);
                        mainActivityView.hideKeyboard();
                        mainActivityView.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mainActivityView.hideProgressBar();
                        disposable.dispose();
                        Log.i(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        disposable.dispose();
                        mainActivityView.hideProgressBar();
                        Log.e(TAG, "onError", e);
                    }
                });
    }

    private void makeToastInternetConn() {
        Toast.makeText(context, NO_NETWORK_CONN_FOUND, Toast.LENGTH_SHORT).show();
    }
}
