package com.dihanov.musiq.ui.main.main_fragments.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface AlbumResultFragmentContract {
    interface View extends BaseView<AlbumResultFragmentContract.Presenter> {
        RecyclerView getRecyclerView();

        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<AlbumResultFragmentContract.View> {
        void addOnSearchBarTextChangedListener(MainActivity fragmentActivity, SearchView searchEditText);
    }
}
