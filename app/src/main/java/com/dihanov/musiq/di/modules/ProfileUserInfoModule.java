package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.settings.profile.user_bio.ProfileUserInfo;
import com.dihanov.musiq.ui.settings.profile.user_bio.ProfileUserInfoContract;
import com.dihanov.musiq.ui.settings.profile.user_bio.ProfileUserInfoPresenter;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ProfileUserInfoModule {
    @Binds
    @PerActivity
    abstract ProfileUserInfoContract.View bindView(ProfileUserInfo profileUserInfo);

    @Binds
    @PerActivity
    abstract ProfileUserInfoContract.Presenter bindPresenter(ProfileUserInfoPresenter profileUserFriendsPresenter);
}
