package com.dihanov.musiq;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.di.app.DaggerAppComponent;
import com.dihanov.musiq.di.modules.NetworkModule;
import com.raizlabs.android.dbflow.config.FlowManager;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class App extends Application implements
        HasActivityInjector,
        HasServiceInjector{
    @Inject
    ScrobbleRepository scrobbleRepository;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
        DaggerAppComponent
                .builder()
                .application(this)
                .networkModule(new NetworkModule(Config.LAST_FM_API_URL))
                .build()
                .inject(this);

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/cabin_regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        if(!userSettingsRepository.hasFavoriteArtists()){
            userSettingsRepository.initFavoriteArtists();
        }
        if(!userSettingsRepository.hasFavoriteAlbums()){
            userSettingsRepository.initFavoriteAlbums();
        }

        scrobbleRepository.scrobbleFromCache();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }


    @Override
    public void onLowMemory() {
        Glide.get(this).clearMemory();
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.get(this).clearMemory();
        super.onTrimMemory(level);
    }
}
