package com.dihanov.musiq.di.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dihanov.musiq.di.modules.LastFmApiServiceModule;
import com.dihanov.musiq.di.modules.NetworkModule;
import com.dihanov.musiq.di.modules.ScrobbleServiceModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

@Module(includes = {NetworkModule.class,
        LastFmApiServiceModule.class,
        ScrobbleServiceModule.class})
public class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }

    //shared preferences for Android context classes
    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }
}
