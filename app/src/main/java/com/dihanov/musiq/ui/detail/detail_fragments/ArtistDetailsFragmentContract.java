package com.dihanov.musiq.ui.detail.detail_fragments;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public interface ArtistDetailsFragmentContract {
    interface View extends BaseView<ArtistDetailsFragmentContract.Presenter> {
    }

    interface Presenter extends BasePresenter<ArtistDetailsFragmentContract.View> {

    }
}
