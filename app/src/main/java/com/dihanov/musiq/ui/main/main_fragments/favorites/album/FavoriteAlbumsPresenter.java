package com.dihanov.musiq.ui.main.main_fragments.favorites.album;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindow;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observable;
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
    private MainContract.View mainActivity;

    @Inject
    public FavoriteAlbumsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(FavoriteAlbumsContract.View view) {
        this.albumResultFragment = view;
        this.mainActivity = albumResultFragment.getMainActivity();
    }

    @Override
    public void leaveView() {
        if (this.compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.albumResultFragment = null;
    }


    @Override
    public void setClickListenerFetchEntireAlbumInfo(AlbumViewHolder viewHolder, String artistName, String albumName) {
        AlbumDetailsPopupWindow albumDetailsPopupWindow = new AlbumDetailsPopupWindow(lastFmApiClient, (MainActivity)mainActivity);
        albumDetailsPopupWindow.showPopupWindow(mainActivity, viewHolder, artistName, albumName, R.id.main_content);
    }

    @Override
    public void loadFavoriteAlbums(Set<String> favorites, RecyclerViewExposable recyclerViewExposable) {
        //resetting the adapter
        recyclerViewExposable.getRecyclerView().setAdapter(new AlbumDetailsAdapter(this.mainActivity, new ArrayList<>(), FavoriteAlbumsPresenter.this, true));

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
                        mainActivity.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Album> albums) {
                        ((AlbumDetailsAdapter) recyclerViewExposable.getRecyclerView().getAdapter()).setArtistAlbumsList(albums);
                        recyclerViewExposable.getRecyclerView().getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }
}
