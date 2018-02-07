package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.scrobble.Scrobbler;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

@Module
public class ScrobblerModule {
    @Provides
    @Singleton
    Scrobbler provideScrobbler(LastFmApiClient lastFmApiClient){
        return new Scrobbler(lastFmApiClient);
    }
}
