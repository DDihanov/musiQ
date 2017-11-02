package com.dihanov.musiq.di.modules;


import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.ui.main.MainActivityPresenter;

import dagger.Binds;
import dagger.Module;


/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class MainActivityModule {
    @Binds
    @PerActivity
    abstract MainActivityContract.View provideMainActivityView(MainActivity mainActivity);

    @Binds
    @PerActivity
    abstract MainActivityContract.Presenter provideMainActivityPresenter(MainActivityPresenter presenter);
}
