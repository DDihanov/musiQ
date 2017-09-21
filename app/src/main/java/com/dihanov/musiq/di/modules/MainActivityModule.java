package com.dihanov.musiq.di.modules;


import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.annotations.PerFragment;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.dihanov.musiq.ui.main.MainActivityPresenter;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentPresenter;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;


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
