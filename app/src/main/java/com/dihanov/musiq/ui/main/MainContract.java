package com.dihanov.musiq.ui.main;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.interfaces.SpecificArtistViewHolderSearchable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainContract {

    interface View extends BaseView<Presenter>, MainViewFunctionable, RecyclerViewExposable{
        SearchView getSearchBar();

        DrawerLayout getDrawerLayout();

        NavigationView getNavigationView();

        void hideKeyboard();

        Context getContext();

        MainActivity getMainActivity();

        void runOnUiThread(Runnable r);
    }

    interface Presenter extends BasePresenter<View>, SpecificArtistViewHolderSearchable{
        void setBackdropImageChangeListener(MainContract.View mainActivity);

        void setOnDrawerOpenedListener(MainContract.View mainActivity);
    }
}
