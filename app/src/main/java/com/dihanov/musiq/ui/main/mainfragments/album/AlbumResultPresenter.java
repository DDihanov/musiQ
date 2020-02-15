package com.dihanov.musiq.ui.main.mainfragments.album;

import com.dihanov.musiq.data.usecase.GetEntireAlbumInfoUseCase;
import com.dihanov.musiq.data.usecase.SearchForAlbumUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.AlbumArtistPairModel;
import com.dihanov.musiq.models.GeneralAlbumSearch;
import com.dihanov.musiq.models.SpecificAlbum;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class AlbumResultPresenter implements
        AlbumResultContract.Presenter {
    private AlbumResultContract.View albumResultFragment;
    private SearchForAlbumUseCase searchForAlbumUseCase;
    private GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase;

    private UseCase.ResultCallback<GeneralAlbumSearch> albumSearchCallback = new UseCase.ResultCallback<GeneralAlbumSearch>() {
        @Override
        public void onStart() {
            albumResultFragment.showProgressBar();
        }

        @Override
        public void onSuccess(GeneralAlbumSearch response) {
            albumResultFragment.setSearchResults(response);
            albumResultFragment.hideKeyboard();
            albumResultFragment.hideProgressBar();
            albumResultFragment.hideProgressBar();
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
        public void onSuccess(SpecificAlbum response) {
            Album fullAlbum = response.getAlbum();
            albumResultFragment.showAlbumDetails(fullAlbum);
            albumResultFragment.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            albumResultFragment.hideProgressBar();
        }
    };

    @Inject
    public AlbumResultPresenter(SearchForAlbumUseCase searchForAlbumUseCase, GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase) {
        this.searchForAlbumUseCase = searchForAlbumUseCase;
        this.getEntireAlbumInfoUseCase = getEntireAlbumInfoUseCase;
    }

    @Override
    public void takeView(AlbumResultContract.View view) {
        this.albumResultFragment = view;
    }

    @Override
    public void leaveView() {
        this.albumResultFragment = null;
    }


    @Override
    public void searchForAlbum(String albumName) {
        searchForAlbumUseCase.invoke(albumSearchCallback, albumName);
    }

    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
        getEntireAlbumInfoUseCase.invoke(specificAlbumResultCallback, new AlbumArtistPairModel(artistName, albumName));
    }

}
