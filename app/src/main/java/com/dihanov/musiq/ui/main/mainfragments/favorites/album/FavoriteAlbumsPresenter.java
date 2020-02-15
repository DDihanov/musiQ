package com.dihanov.musiq.ui.main.mainfragments.favorites.album;

import com.dihanov.musiq.data.usecase.DeserializeFavoriteAlbumsUseCase;
import com.dihanov.musiq.data.usecase.GetEntireAlbumInfoUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.AlbumArtistPairModel;
import com.dihanov.musiq.models.SpecificAlbum;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class FavoriteAlbumsPresenter implements FavoriteAlbumsContract.Presenter{
    private FavoriteAlbumsContract.View albumResultFragment;
    private DeserializeFavoriteAlbumsUseCase deserializeFavoriteAlbumsUseCase;
    private GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase;

    private UseCase.ResultCallback<List<Album>> deserializedAlbumsCallBack = new UseCase.ResultCallback<List<Album>>() {
        @Override
        public void onStart() {
            albumResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(List<Album> albums) {
            albumResultFragment.setArtistAlbumsList(albums);
            albumResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            albumResultFragment.hideProgressBar();
        }
    };
    private UseCase.ResultCallback<SpecificAlbum> specificAlbumResultCallback = new UseCase.ResultCallback<SpecificAlbum>() {
        @Override
        public void onStart() {
            albumResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(SpecificAlbum specificAlbum) {
            Album fullAlbum = specificAlbum.getAlbum();
            albumResultFragment.showAlbumDetails(fullAlbum);
            albumResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            albumResultFragment.hideProgressBar();
        }
    };

    @Inject
    public FavoriteAlbumsPresenter(DeserializeFavoriteAlbumsUseCase deserializeFavoriteAlbumsUseCase, GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase) {
        this.deserializeFavoriteAlbumsUseCase = deserializeFavoriteAlbumsUseCase;
        this.getEntireAlbumInfoUseCase = getEntireAlbumInfoUseCase;
    }

    @Override
    public void takeView(FavoriteAlbumsContract.View view) {
        this.albumResultFragment = view;
    }

    @Override
    public void leaveView() {
        this.albumResultFragment = null;
    }

    @Override
    public void loadFavoriteAlbums(Set<String> favorites) {
        deserializeFavoriteAlbumsUseCase.invoke(deserializedAlbumsCallBack, favorites);
    }

    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
       getEntireAlbumInfoUseCase.invoke(specificAlbumResultCallback, new AlbumArtistPairModel(artistName, albumName));
    }
}
