package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.mainfragments.usertoptracks.UserTopTracks;
import com.dihanov.musiq.ui.main.mainfragments.usertoptracks.UserTopTracksContract;
import com.dihanov.musiq.ui.main.mainfragments.usertoptracks.UserTopTracksPresenter;

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
