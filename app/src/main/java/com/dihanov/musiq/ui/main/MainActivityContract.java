package com.dihanov.musiq.ui.main;

import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        void showProgressBar();

        void hideProgressBar();

        void hideKeyboard();
    }

    interface Presenter extends BasePresenter<View>{
        void addOnAutoCompleteTextViewTextChangedObserver(RecyclerView recyclerView, EditText searchEditText);

        void addOnAutoCompleteTextViewItemClickedSubscriber(RecyclerView recyclerView);
    }
}
