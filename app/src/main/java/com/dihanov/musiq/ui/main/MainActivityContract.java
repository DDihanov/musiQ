package com.dihanov.musiq.ui.main;

import android.widget.AutoCompleteTextView;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View>{
        void addOnAutoCompleteTextViewTextChangedObserver(AutoCompleteTextView autoCompleteTextView);

        void addOnAutoCompleteTextViewItemClickedSubscriber(AutoCompleteTextView autoCompleteTextView);
    }
}
