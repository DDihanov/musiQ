package com.dihanov.musiq.di.modules;


import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.ui.main.MainPresenter;

import dagger.Binds;
import dagger.Module;


/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class MainActivityModule {
    @Binds
    @PerActivity
    abstract MainContract.View provideMainActivityView(MainActivity mainActivity);

    @Binds
    @PerActivity
    abstract MainContract.Presenter provideMainActivityPresenter(MainPresenter presenter);
}
