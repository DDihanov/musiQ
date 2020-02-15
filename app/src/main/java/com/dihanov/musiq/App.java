package com.dihanov.musiq;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.di.app.DaggerAppComponent;
import com.dihanov.musiq.di.modules.NetworkModule;
import com.dihanov.musiq.service.scrobble.Scrobbler;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class App extends Application implements
        HasActivityInjector,
        HasServiceInjector{
    @Inject
    Scrobbler scrobbler;

    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
        FlowManager.init(this);
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

        if(!userSettingsRepository.hasFavoriteArtists()){
            userSettingsRepository.initFavoriteArtists();
        }
        if(!userSettingsRepository.hasFavoriteAlbums()){
            userSettingsRepository.initFavoriteAlbums();
        }

        scrobbler.scrobbleFromCache();
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
