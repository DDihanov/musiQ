package com.dihanov.musiq.di.modules;


import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.ui.main.MainActivityPresenter;


import javax.annotation.Nullable;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class MainActivityModule {
    @Binds
    abstract MainActivityContract.Presenter taskPresenter(MainActivityPresenter presenter);

    @Provides
    @Binds
    @ContributesAndroidInjector
    abstract MainActivityContract.View provideMainView(MainActivity mainActivity);
}
