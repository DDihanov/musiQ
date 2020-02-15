package com.dihanov.musiq.ui.main.mainfragments.album;

import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class AlbumResultPresenter implements AlbumResultContract.Presenter {
    private static final int limit = 20;

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private AlbumResultContract.View albumResultFragment;

    @Inject
    public AlbumResultPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(AlbumResultContract.View view) {
        this.albumResultFragment = view;
    }

    @Override
    public void leaveView() {
        if(this.compositeDisposable != null){
            compositeDisposable.clear();
        }
        this.albumResultFragment = null;
    }


    @Override
    public Observable<GeneralAlbumSearch> searchForAlbum(String albumName) {
        return lastFmApiClient.getLastFmApiService()
                .searchForAlbum(albumName, limit);
    }

    @Override
    public void publishResult(Observable<GeneralAlbumSearch> observable) {
        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<GeneralAlbumSearch>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        albumResultFragment.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(GeneralAlbumSearch albumSearchResults) {
                        albumResultFragment.setSearchResults(albumSearchResults);
                        albumResultFragment.hideKeyboard();
                        albumResultFragment.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        albumResultFragment.hideProgressBar();
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onError(Throwable e) {
                        albumResultFragment.hideProgressBar();
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
                        albumResultFragment.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SpecificAlbum specificAlbum) {
                        Album fullAlbum = specificAlbum.getAlbum();
                        albumResultFragment.showAlbumDetails(fullAlbum);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        albumResultFragment.hideProgressBar();
                    }
                });
    }
}
