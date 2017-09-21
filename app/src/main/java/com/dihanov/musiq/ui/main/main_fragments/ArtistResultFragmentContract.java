package com.dihanov.musiq.ui.main.main_fragments;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public interface ArtistResultFragmentContract {
    interface View extends BaseView<Presenter> {
        RecyclerView getRecyclerView();

        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void addOnArtistResultClickedListener(RecyclerView recyclerView);

        void addOnTextViewTextChangedObserver(MainActivity fragmentActivity, EditText searchEditText);

        void setRecyclerViewAdapter(ArtistAdapter adapter);
    }
}
