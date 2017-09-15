package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.service.LastFmApiClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public class LastFmApiServiceModule {
    @Provides
    @Singleton
    LastFmApiClient provideRestApiClient(Retrofit retrofit){
        return new LastFmApiClient(retrofit);
    }
}
