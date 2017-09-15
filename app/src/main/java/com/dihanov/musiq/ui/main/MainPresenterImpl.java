package com.dihanov.musiq.ui.main;

import com.dihanov.musiq.service.LastFmApiClient;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 25/05/2017.
 */

public class MainPresenterImpl implements MainPresenter{
    MainView mainView;
    LastFmApiClient lastFmApiClient;

    @Inject
    public MainPresenterImpl(MainView mainView, LastFmApiClient lastFmApiClient) {
        this.mainView = mainView;
        this.lastFmApiClient = lastFmApiClient;
    }

    public void loadMain(){
//        apiService.loadData();
        mainView.onMainLoaded();
    }
}
