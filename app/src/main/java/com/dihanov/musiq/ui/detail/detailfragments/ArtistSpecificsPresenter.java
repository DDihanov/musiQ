package com.dihanov.musiq.ui.detail.detailfragments;

import com.dihanov.musiq.data.usecase.GetEntireAlbumInfoUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.AlbumArtistPairModel;
import com.dihanov.musiq.models.SpecificAlbum;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistSpecificsPresenter implements
        ArtistSpecificsContract.Presenter, UseCase.ResultCallback<SpecificAlbum> {
    private ArtistSpecificsContract.View artistDetailsFragment;
    private GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase;
    @Inject
    public ArtistSpecificsPresenter(GetEntireAlbumInfoUseCase getEntireAlbumInfoUseCase) {
        this.getEntireAlbumInfoUseCase = getEntireAlbumInfoUseCase;
    }

    @Override
    public void takeView(ArtistSpecificsContract.View view) {
        this.artistDetailsFragment = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsFragment = null;
    }


    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
       getEntireAlbumInfoUseCase.invoke(this, new AlbumArtistPairModel(artistName, albumName));
    }

    @Override
    public void onStart() {
        artistDetailsFragment.showProgressBar();
    }

    @Override
    public void onSuccess(SpecificAlbum response) {
        Album fullAlbum = response.getAlbum();
        artistDetailsFragment.showAlbumDetails(fullAlbum);
        artistDetailsFragment.hideProgressBar();
    }

    @Override
    public void onError(Throwable e) {
        artistDetailsFragment.hideProgressBar();
    }
}
