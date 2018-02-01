package com.dihanov.musiq.di.app;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.modules.NetworkModule;
import com.dihanov.musiq.util.Constants;

import java.util.HashSet;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class App extends Application implements HasActivityInjector{
    private static SharedPreferences sharedPreferences;

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
                .setDefaultFontPath("fonts/cabin_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!sharedPreferences.contains(Constants.FAVORITE_ARTISTS_KEY)){
            sharedPreferences.edit().putStringSet(Constants.FAVORITE_ARTISTS_KEY, new HashSet<>()).apply();
        }
        if(!sharedPreferences.contains(Constants.FAVORITE_ALBUMS_KEY)){
            sharedPreferences.edit().putStringSet(Constants.FAVORITE_ALBUMS_KEY, new HashSet<>()).apply();
        }
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    //method for getting SP outside of Android classes:
    public static SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}
