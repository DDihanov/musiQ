package com.dihanov.musiq.ui.main;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainActivityContract {

    interface View extends BaseView<Presenter>{
        void search();

        void showProgressBar();

        void hideProgressBar();

        void refreshSearchBar();
    }

    interface Presenter extends BasePresenter<View>{
        void addDataToSearchList(String searchQuery, List<String> searchList);
        void setSearchListener(AutoCompleteTextView v);
    }
}
