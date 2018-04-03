package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.settings.profile.user_friends_info.ProfileUserFriendsContract;
import com.dihanov.musiq.ui.settings.profile.user_friends_info.ProfileUserFriendsInfo;
import com.dihanov.musiq.ui.settings.profile.user_friends_info.ProfileUserFriendsPresenter;

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
