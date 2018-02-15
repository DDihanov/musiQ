package com.dihanov.musiq.ui.main.main_fragments.album;

import android.app.Activity;
import android.support.v7.widget.SearchView;
import android.util.Log;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindow;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView;

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
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class AlbumResultFragmentPresenter implements AlbumResultFragmentContract.Presenter, SpecificAlbumSearchable {
    private static final long DELAY_IN_MILLIS = 500;
    private static final int limit = 20;
    private static final long ALBUM_LOADED_THREAD_TIMEOUT = 2000L;

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AlbumResultFragmentContract.View albumResultFragment;
    private MainActivityContract.View mainActivity;

    @Inject
    public AlbumResultFragmentPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(AlbumResultFragmentContract.View view) {
        this.albumResultFragment = view;
        this.mainActivity = albumResultFragment.getMainActivity();
    }

    @Override
    public void leaveView() {
        if(this.compositeDisposable != null){
            compositeDisposable.clear();
        }
        this.albumResultFragment = null;
    }



    @Override
    public void addOnSearchBarTextChangedListener(MainActivity fragmentActivity, SearchView searchEditText) {
        AlbumResultFragmentPresenter albumResultFragmentPresenter = this;
        if(searchEditText == null){
            return;
        }
        Observable<GeneralAlbumSearch> autocompleteResponseObservable =
                RxSearchView.queryTextChanges(searchEditText)
                        .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                        .filter(s -> s.length() >= 2)
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(s -> {
                            HelperMethods.checkConnection((MainActivity)mainActivity);
                            mainActivity.showProgressBar();
                        })
                        .observeOn(Schedulers.io())
                        .flatMap(new Function<CharSequence, Observable<GeneralAlbumSearch>>() {
                            @Override
                            public Observable<GeneralAlbumSearch> apply(CharSequence s) throws Exception {
                                return lastFmApiClient.getLastFmApiService()
                                        .searchForAlbum(s.toString(), limit);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .retry();


        autocompleteResponseObservable
                .subscribe(new Observer<GeneralAlbumSearch>() {
                    private static final String TAG = "AlbumResult";

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(GeneralAlbumSearch albumSearchResults) {
                        Log.i(TAG, albumSearchResults.toString());

                        List<Album> result = new ArrayList<>();
                        result.addAll(albumSearchResults.getResults().getAlbummatches().getAlbum());
                        if(result.isEmpty()){
                            result = Collections.emptyList();
                        }

                        AlbumDetailsAdapter albumAdapter = new AlbumDetailsAdapter((Activity)mainActivity, result, albumResultFragmentPresenter);

                        albumResultFragment.getRecyclerView().setAdapter(albumAdapter);
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
    public void setClickListenerFetchEntireAlbumInfo(AlbumViewHolder viewHolder, String artistName, String albumName) {
        AlbumDetailsPopupWindow albumDetailsPopupWindow = new AlbumDetailsPopupWindow(lastFmApiClient, (Activity)mainActivity);
        albumDetailsPopupWindow.showPopupWindow(mainActivity, viewHolder, artistName, albumName, R.id.main_content);
    }
}
