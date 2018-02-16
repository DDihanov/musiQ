package com.dihanov.musiq.ui.main.main_fragments.artist;

import android.content.Context;

import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainContract;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public interface ArtistResultContract {
    interface View extends BaseView<Presenter>, RecyclerViewExposable {
        MainActivity getMainActivity();

        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void addOnSearchBarTextChangedListener(MainContract.View fragmentActivity);
    }
}
