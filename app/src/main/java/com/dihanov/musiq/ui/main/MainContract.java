package com.dihanov.musiq.ui.main;

import android.content.Context;
import android.support.v7.widget.SearchView;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainContract {

    interface View extends BaseView<Presenter>, MainViewFunctionable, RecyclerViewExposable{
        SearchView getSearchBar();

        void hideKeyboard();

        Context getContext();

        MainActivity getMainActivity();
    }

    interface Presenter extends BasePresenter<View>{
        void setBackdropImageChangeListener(MainContract.View mainActivity);
    }
}
