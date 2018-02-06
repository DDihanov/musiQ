package com.dihanov.musiq.service;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class Scrobbler {

    private LastFmApiClient lastFmApiClient;

    public Scrobbler(LastFmApiClient lastFmApiClient){
        this.lastFmApiClient = lastFmApiClient;
    }


}
