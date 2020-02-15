package com.dihanov.musiq.ui.main.mainfragments.favorites.album;

import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class FavoriteAlbumsPresenter implements FavoriteAlbumsContract.Presenter{
    private static final int LIMIT = 1;

    private final LastFmApiClient lastFmApiClient;

    private FavoriteAlbumsContract.View albumResultFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public FavoriteAlbumsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(FavoriteAlbumsContract.View view) {
        this.albumResultFragment = view;
    }

    @Override
    public void leaveView() {
        if (this.compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.albumResultFragment = null;
    }

    @Override
    public void loadFavoriteAlbums(Set<String> favorites) {
        List<Album> deserializedList = new ArrayList<>();

        for (String serializedAlbum : favorites) {
            String actualValue = serializedAlbum.split(" \\_\\$\\_ ")[1];
            Album album = new Gson().fromJson(actualValue, Album.class);
            deserializedList.add(album);
        }

        Single.just(deserializedList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Album>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        albumResultFragment.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Album> albums) {
                        albumResultFragment.setArtistAlbumsList(albums);
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
