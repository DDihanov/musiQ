package com.dihanov.musiq.ui.main.mainfragments.favorites.artist;

import com.dihanov.musiq.data.usecase.DeserializeFavoriteArtistsUseCase;
import com.dihanov.musiq.data.usecase.FetchArtistWithSerializedInfoUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Artist;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class FavoriteArtistsPresenter implements FavoriteArtistsContract.Presenter {
    private FavoriteArtistsContract.View artistResultFragment;
    private DeserializeFavoriteArtistsUseCase deserializeFavoriteArtistsUseCase;
    private FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase;

    private UseCase.ResultCallback<List<Artist>> deserializedArtistCallback = new UseCase.ResultCallback<List<Artist>>() {
        @Override
        public void onStart() {
            artistResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(List<Artist> artists) {
            artistResultFragment.setArtistList(artists);
            artistResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            artistResultFragment.hideProgressBar();
        }

    };

    private UseCase.ResultCallback<HashMap<String, String>> fetchedSerializedArtists = new UseCase.ResultCallback<HashMap<String, String>>() {
        @Override
        public void onStart() {
            artistResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(HashMap<String, String> response) {
            artistResultFragment.startActivityWithExtras(response);
            artistResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            artistResultFragment.hideProgressBar();
        }
    };

    @Inject
    public FavoriteArtistsPresenter(DeserializeFavoriteArtistsUseCase deserializeFavoriteArtistsUseCase, FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase) {
        this.fetchArtistWithSerializedInfoUseCase = fetchArtistWithSerializedInfoUseCase;
        this.deserializeFavoriteArtistsUseCase = deserializeFavoriteArtistsUseCase;
    }

    @Override
    public void takeView(FavoriteArtistsContract.View view) {
        this.artistResultFragment = view;
    }

    @Override
    public void leaveView() {
        this.artistResultFragment = null;
    }

    @Override
    public void loadFavoriteArtists(Set<String> favorites) {
        deserializeFavoriteArtistsUseCase.invoke(deserializedArtistCallback, favorites);
    }

    @Override
    public void fetchArtist(String artistName) {
       fetchArtistWithSerializedInfoUseCase.invoke(fetchedSerializedArtists, artistName);
    }
}
