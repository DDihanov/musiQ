package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.settings.profile.userlovedtracks.UserLovedTracksContract;
import com.dihanov.musiq.ui.settings.profile.userlovedtracks.UserLovedTracksPresenter;
import com.dihanov.musiq.ui.settings.profile.userlovedtracks.UserLovedTracksView;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class UserLovedTracksModule {
    @Binds
    @PerActivity
    abstract UserLovedTracksContract.View bindUserLovedTracksView(UserLovedTracksView view);

    @Binds
    @PerActivity
    abstract UserLovedTracksContract.Presenter bindUserLovedTracksPresenter(UserLovedTracksPresenter presenter);
}
