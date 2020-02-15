package com.dihanov.musiq.ui.main.mainfragments.artist;

import com.dihanov.musiq.data.usecase.FetchArtistWithSerializedInfoUseCase;
import com.dihanov.musiq.data.usecase.SearchForArtistUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.ArtistSearchResults;

import java.util.HashMap;

import javax.inject.Inject;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultPresenter implements ArtistResultContract.Presenter{
    private ArtistResultContract.View artistResultFragment;
    private SearchForArtistUseCase searchForArtistUseCase;
    private FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase;

    private UseCase.ResultCallback<ArtistSearchResults> artistSearchResultsResultCallback = new UseCase.ResultCallback<ArtistSearchResults>() {
        @Override
        public void onStart() {
            artistResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(ArtistSearchResults response) {
            artistResultFragment.setSearchResults(response);
            artistResultFragment.hideKeyboard();
            artistResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            artistResultFragment.hideProgressBar();
        }
    };

    private UseCase.ResultCallback<HashMap<String, String>> fetchArtistWithSerializedInfoCallback = new UseCase.ResultCallback<HashMap<String, String>>() {
        @Override
        public void onStart() {
            artistResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(HashMap<String, String> response) {
            artistResultFragment.hideProgressBar();
            artistResultFragment.startActivityWithExtras(response);
        }

        @Override
        public void onError(Throwable e) {
            artistResultFragment.hideProgressBar();
        }
    };

    @Inject
    public ArtistResultPresenter(SearchForArtistUseCase searchForArtistUseCase, FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase) {
        this.searchForArtistUseCase = searchForArtistUseCase;
        this.fetchArtistWithSerializedInfoUseCase = fetchArtistWithSerializedInfoUseCase;
    }

    @Override
    public void takeView(ArtistResultContract.View view) {
        this.artistResultFragment = view;
    }

    @Override
    public void leaveView() {
        this.artistResultFragment = null;
    }


    @Override
    public void searchForArtist(String artistName) {
        searchForArtistUseCase.invoke(artistSearchResultsResultCallback, artistName);
    }

    @Override
    public void fetchArtist(String artistName) {
        fetchArtistWithSerializedInfoUseCase.invoke(fetchArtistWithSerializedInfoCallback, artistName);
    }
}
