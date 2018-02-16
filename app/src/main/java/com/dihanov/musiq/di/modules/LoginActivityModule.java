package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.login.Login;
import com.dihanov.musiq.ui.login.LoginContract;
import com.dihanov.musiq.ui.login.LoginPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

@Module
public abstract class LoginActivityModule {
    @Binds
    @PerActivity
    abstract LoginContract.View bindLoginActivityView(Login login);

    @Binds
    @PerActivity
    abstract LoginContract.Presenter bindLoginActivityPresenter(LoginPresenter loginPresenter);
}
