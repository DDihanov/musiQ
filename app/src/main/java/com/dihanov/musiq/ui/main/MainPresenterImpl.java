package com.dihanov.musiq.ui.main;

import com.dihanov.musiq.service.RestClient;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 25/05/2017.
 */

public class MainPresenterImpl implements MainPresenter{
    MainView mainView;
    RestClient restClient;

    @Inject
    public MainPresenterImpl(MainView mainView, RestClient restClient) {
        this.mainView = mainView;
        this.restClient = restClient;
    }

    public void loadMain(){
//        apiService.loadData();
        mainView.onMainLoaded();
    }
}
