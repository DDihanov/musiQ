package com.dihanov.musiq.ui.main.main_fragments.favorites.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

import java.util.Set;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface FavoriteAlbumsFragmentContract {
    interface View extends BaseView<FavoriteAlbumsFragmentContract.Presenter> {
        RecyclerView getRecyclerView();

        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<FavoriteAlbumsFragmentContract.View>, SpecificAlbumSearchable {
        void loadFavoriteAlbums(Set<String> favorites, MainViewFunctionable mainViewFunctionable, RecyclerView recyclerView);
    }
}
