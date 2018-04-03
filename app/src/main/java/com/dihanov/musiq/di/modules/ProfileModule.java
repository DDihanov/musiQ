package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.settings.profile.Profile;
import com.dihanov.musiq.ui.settings.profile.ProfileContract;
import com.dihanov.musiq.ui.settings.profile.ProfilePresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProfileModule {
    @Binds
    @PerActivity
    abstract ProfileContract.View provideProfileView(Profile profile);

    @Binds
    @PerActivity
    abstract ProfileContract.Presenter provideProfilePresenter(ProfilePresenter profilePresenter);
}
