package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.db.ScrobbleDB;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dimitar.dihanov on 2/8/2018.
 */

@Module
public class DbModule {
    @Singleton
    @Provides
    ScrobbleDB provideScrobbleDb(){
        return new ScrobbleDB();
    }
}
