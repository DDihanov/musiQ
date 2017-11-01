package com.dihanov.musiq.di.app;

import android.app.Activity;
import android.app.Application;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.modules.NetworkModule;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

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
                .networkModule(new NetworkModule(Config.LAST_FM_API_URL))
                .build()
                .inject(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("assets/fonts/cabin_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
