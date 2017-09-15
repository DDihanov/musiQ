package com.dihanov.musiq.ui.main;

import com.dihanov.musiq.service.LastFmApiService;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 25/05/2017.
 */

public class MainPresenterImpl implements MainPresenter{
    MainView mainView;
    LastFmApiService apiService;

    @Inject
    public MainPresenterImpl(MainView mainView, LastFmApiService apiService) {
        this.mainView = mainView;
        this.apiService = apiService;
    }

    public void loadMain(){
//        apiService.loadData();
        mainView.onMainLoaded();
    }
}
