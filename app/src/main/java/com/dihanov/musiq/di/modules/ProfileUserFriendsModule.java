package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.settings.profile.userfriendsinfo.ProfileUserFriendsContract;
import com.dihanov.musiq.ui.settings.profile.userfriendsinfo.ProfileUserFriendsInfo;
import com.dihanov.musiq.ui.settings.profile.userfriendsinfo.ProfileUserFriendsPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProfileUserFriendsModule {
    @Binds
    @PerActivity
    abstract ProfileUserFriendsContract.View bindView(ProfileUserFriendsInfo profileUserFriendsInfo);

    @Binds
    @PerActivity
    abstract ProfileUserFriendsContract.Presenter bindPresenter(ProfileUserFriendsPresenter profileUserFriendsPresenter);
}
