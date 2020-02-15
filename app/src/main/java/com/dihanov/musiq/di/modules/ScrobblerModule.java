package com.dihanov.musiq.di.modules;

import android.content.Context;

import com.dihanov.musiq.db.ScrobbleDB;
import com.dihanov.musiq.db.UserSettingsRepository;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.scrobble.Scrobbler;
import com.dihanov.musiq.util.SigGenerator;

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
    Scrobbler provideScrobbler(LastFmApiClient lastFmApiClient, Context context, ScrobbleDB scrobbleDB, UserSettingsRepository userSettingsRepository, SigGenerator sigGenerator){
        return new Scrobbler(lastFmApiClient, context, scrobbleDB, userSettingsRepository);
    }
}
