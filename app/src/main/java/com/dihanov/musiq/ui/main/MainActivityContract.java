package com.dihanov.musiq.ui.main;

import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.widget.SearchView;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        SearchView getSearchBar();

        HorizontalGridView getGridView();

        void showProgressBar();

        void hideProgressBar();

        void hideKeyboard();

        android.view.View getBirdIcon();
    }

    interface Presenter extends BasePresenter<View>{
        void setBackdropImageChangeListener(MainActivity mainActivity);
    }
}
