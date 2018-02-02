package com.dihanov.musiq.ui.main.main_fragments.favorites.artist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.Set;

public interface FavoriteArtistsFragmentContract {
    interface View extends BaseView<Presenter> {
        RecyclerView getRecyclerView();

        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void loadFavoriteArtists(Set<String> favorites, MainViewFunctionable mainViewFunctionable, RecyclerView recyclerView);
    }
}