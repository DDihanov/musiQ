package com.dihanov.musiq.ui.main;

import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        EditText getSearchBar();

        void showProgressBar();

        void hideProgressBar();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter<View>{

    }
}
