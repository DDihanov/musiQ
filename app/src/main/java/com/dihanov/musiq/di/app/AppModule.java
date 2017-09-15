package com.dihanov.musiq.di.app;

import android.app.Application;
import android.content.Context;

import com.dihanov.musiq.di.components.DetailActivityComponent;
import com.dihanov.musiq.di.components.MainActivityComponent;
import com.dihanov.musiq.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

@Module(subcomponents = {
        MainActivityComponent.class,
        DetailActivityComponent.class},
        includes = NetworkModule.class)
public class AppModule {
    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application;
    }
}
