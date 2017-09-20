package com.dihanov.musiq.ui.main;


import javax.inject.Inject;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    MainActivityContract.View mainActivityView;

    @Inject
    public MainActivityPresenter() {
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        this.mainActivityView = null;
    }
}
