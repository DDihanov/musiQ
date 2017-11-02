package com.dihanov.musiq.ui.main;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        SearchView getSearchBar();

        RecyclerView getRecyclerView();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter<View>{
        void setBackdropImageChangeListener(MainActivity mainActivity);
    }
}
