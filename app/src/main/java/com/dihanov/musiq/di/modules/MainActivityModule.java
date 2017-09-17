package com.dihanov.musiq.di.modules;


import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.ui.main.MainActivityPresenter;

import javax.inject.Inject;

import dagger.Binds;
import dagger.Module;


/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class MainActivityModule {
    @Binds
    abstract MainActivityContract.Presenter provideMainActivityPresenter(MainActivityPresenter presenter);

    @Binds
    abstract MainActivityContract.View provideMainActivityView(MainActivity mainActivity);
}
