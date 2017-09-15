package com.dihanov.musiq.di.app;

import android.app.Activity;
import android.app.Application;

import com.dihanov.musiq.config.Config;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class App extends Application implements HasActivityInjector{
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent
                .builder()
                .application(this)
                .networkModule(Config.LAST_FM_API_URL)
                .build()
                .inject(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
