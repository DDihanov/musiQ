package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.service.LastFmApiService;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 30/05/2017.
 */

public class DetailPresenterImpl implements DetailPresenter{

    DetailView detailView;
    LastFmApiService apiService;

    @Inject
    public DetailPresenterImpl(DetailView detailView, LastFmApiService apiService) {
        this.detailView = detailView;
        this.apiService = apiService;
    }

    public void loadDetail(){
//        apiService.loadData();
        detailView.onDetailLoaded();
    }
}
