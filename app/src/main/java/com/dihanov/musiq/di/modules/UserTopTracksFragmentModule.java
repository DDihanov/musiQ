package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.main_fragments.user_top_tracks.UserTopTracks;
import com.dihanov.musiq.ui.main.main_fragments.user_top_tracks.UserTopTracksContract;
import com.dihanov.musiq.ui.main.main_fragments.user_top_tracks.UserTopTracksPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

@Module
public abstract class UserTopTracksFragmentModule {
    @Binds
    @PerActivity
    abstract UserTopTracksContract.View provideUserTopTracksView(UserTopTracks userTopTracks);

    @Binds
    @PerActivity
    abstract UserTopTracksContract.Presenter provideUserTopTracksPresenter(UserTopTracksPresenter userTopTracksPresenter);
}
