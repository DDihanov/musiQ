package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.service.LastFmApiClient;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 30/05/2017.
 */

public class DetailPresenterImpl implements DetailPresenter{

    DetailView detailView;
    LastFmApiClient lastFmApiClient;

    @Inject
    public DetailPresenterImpl(DetailView detailView, LastFmApiClient lastFmApiClient) {
        this.detailView = detailView;
        this.lastFmApiClient = lastFmApiClient;
    }

    public void loadDetail(){
//        apiService.loadData();
        detailView.onDetailLoaded();
    }
}
