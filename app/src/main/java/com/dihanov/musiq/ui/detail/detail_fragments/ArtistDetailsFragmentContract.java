package com.dihanov.musiq.ui.detail.detail_fragments;

import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public interface ArtistDetailsFragmentContract {
    interface View extends BaseView<ArtistDetailsFragmentContract.Presenter> {
        ArtistDetailsActivity getArtistDetailsActivity();
    }

    interface Presenter extends BasePresenter<ArtistDetailsFragmentContract.View>, SpecificAlbumSearchable {

    }
}
