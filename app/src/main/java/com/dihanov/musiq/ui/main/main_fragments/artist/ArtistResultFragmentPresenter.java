package com.dihanov.musiq.ui.main.main_fragments.artist;

import android.support.v7.widget.SearchView;
import android.util.Log;

import com.dihanov.musiq.interfaces.ArtistDetailsIntentShowableImpl;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultFragmentPresenter extends ArtistDetailsIntentShowableImpl implements ArtistResultFragmentContract.Presenter{
    private static final long DELAY_IN_MILLIS = 500;
    private static final int limit = 20;

    private final LastFmApiClient lastFmApiClient;

    private ArtistResultFragment artistResultFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainActivityContract.View mainActivity;

    @Inject
    public ArtistResultFragmentPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistResultFragmentContract.View view) {
        this.artistResultFragment = (ArtistResultFragment) view;
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
                            HelperMethods.checkConnection(mainActivity);
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
                .subscribe(new Observer<ArtistSearchResults>() {
                    private static final String TAG = "ArtistResult";

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArtistSearchResults artistSearchResults) {
                        Log.i(TAG, artistSearchResults.toString());

                        List<Artist> result = new ArrayList<>();
                        result.addAll(artistSearchResults.getResults().getArtistmatches().getArtistMatches());
                        if(result.isEmpty()){
                            result = Collections.emptyList();
                        }

                        ArtistAdapter artistAdapter = new ArtistAdapter(mainActivity, result, artistResultFragmentPresenter);

                        mainActivity.getRecyclerView().setAdapter(artistAdapter);
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

    @Override
    public void addOnArtistResultClickedListener(ClickableArtistViewHolder viewHolder, String artistName) {
        RxView.clicks(viewHolder.getThumbnail())
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    this.showArtistDetailsIntent(artistName, mainActivity);
                });
    }
}
