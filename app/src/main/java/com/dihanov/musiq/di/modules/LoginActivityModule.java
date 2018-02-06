package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.login.LoginActivity;
import com.dihanov.musiq.ui.login.LoginActivityContract;
import com.dihanov.musiq.ui.login.LoginActivityPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

@Module
public abstract class LoginActivityModule {
    @Binds
    @PerActivity
    abstract LoginActivityContract.View bindLoginActivityView(LoginActivity loginActivity);

    @Binds
    @PerActivity
    abstract LoginActivityContract.Presenter bindLoginActivityPresenter(LoginActivityPresenter loginActivityPresenter);
}
