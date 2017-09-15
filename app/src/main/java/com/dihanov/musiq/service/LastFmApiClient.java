package com.dihanov.musiq.service;

import retrofit2.Retrofit;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public class ApiClient {
    LastFmApiService lastFmApiService;

    public ApiClient(Retrofit retrofit){
        lastFmApiService = retrofit.create(LastFmApiService.class);
    }

    public LastFmApiService getLastFmApiService(){
        return lastFmApiService;
    }
}
