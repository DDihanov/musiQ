package com.dihanov.musiq.ui.detail.detail_fragments;

import com.dihanov.musiq.interfaces.SpecificAlbumViewHolderClickable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.detail.ArtistDetails;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public interface ArtistSpecificsContract {
    interface View extends BaseView<ArtistSpecificsContract.Presenter>{
        ArtistDetails getArtistDetailsActivity();

    }

    interface Presenter extends BasePresenter<ArtistSpecificsContract.View>, SpecificAlbumViewHolderClickable {

    }
}
