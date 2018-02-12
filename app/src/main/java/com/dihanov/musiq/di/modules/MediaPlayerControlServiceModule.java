package com.dihanov.musiq.di.modules;

import android.app.Service;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.service.MediaControllerListenerService;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

@Module
public class MediaPlayerControlServiceModule {
    @Provides
    @PerActivity
    Service provideService(MediaControllerListenerService mediaControllerListenerService){
        return mediaControllerListenerService;
    }
}
