package com.dihanov.musiq.ui.main.main_fragments.album;

import android.content.Context;

import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainContract;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface AlbumResultContract {
    interface View extends BaseView<AlbumResultContract.Presenter>, RecyclerViewExposable {
        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<AlbumResultContract.View>, SpecificAlbumSearchable {
        void addOnSearchBarTextChangedListener(MainContract.View fragmentActivity);
    }
}
