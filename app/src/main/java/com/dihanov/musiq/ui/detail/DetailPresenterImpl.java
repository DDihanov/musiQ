package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.service.RestClient;

import javax.inject.Inject;

/**
 * Created by mertsimsek on 30/05/2017.
 */

public class DetailPresenterImpl implements DetailPresenter{

    DetailView detailView;
    RestClient restClient;

    @Inject
    public DetailPresenterImpl(DetailView detailView, RestClient restClient) {
        this.detailView = detailView;
        this.restClient = restClient;
    }

    public void loadDetail(){
//        apiService.loadData();
        detailView.onDetailLoaded();
    }
}
