package com.dihanov.musiq.di.service;

import dagger.Module;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

@Module
public class NetworkModule {
    private String baseUr;

    public NetworkModule(String baseUr) {
        this.baseUr = baseUr;
    }

    
}
