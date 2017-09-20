package com.dihanov.musiq.ui.main.main_fragments;

import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivityContract;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public interface ArtistResultFragmentContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter<MainActivityContract.View> {
        void addOnArtistResultClickedListener(RecyclerView recyclerView);
    }
}
