package com.dihanov.musiq.di.modules;

import android.content.Context;

import com.dihanov.musiq.data.db.ScrobbleDB;
import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.data.usecase.BulkScrobbleUseCase;
import com.dihanov.musiq.data.usecase.ScrobbleTrackUseCase;
import com.dihanov.musiq.data.usecase.UpdateNowPlayingUseCase;
import com.dihanov.musiq.util.Notificator;

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
    ScrobbleRepository provideScrobbler(UpdateNowPlayingUseCase updateNowPlayingUseCase, BulkScrobbleUseCase bulkScrobbleUseCase, ScrobbleTrackUseCase scrobbleTrackUseCase, Context context, ScrobbleDB scrobbleDB, UserSettingsRepository userSettingsRepository, Notificator notificator){
        return new ScrobbleRepository(updateNowPlayingUseCase, bulkScrobbleUseCase, scrobbleTrackUseCase, context, scrobbleDB, userSettingsRepository, notificator);
    }
}
