package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.mainfragments.usertopartists.UserTopArtists;
import com.dihanov.musiq.ui.main.mainfragments.usertopartists.UserTopArtistsContract;
import com.dihanov.musiq.ui.main.mainfragments.usertopartists.UserTopArtistsPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

@Module
public abstract class UserTopArtistsFragmentModule {
    @Binds
    @PerActivity
    abstract UserTopArtistsContract.View provideUserTopArtistsView(UserTopArtists userTopArtists);

    @Binds
    @PerActivity
    abstract UserTopArtistsContract.Presenter provideUserTopArtistsPresenter(UserTopArtistsPresenter userTopArtistsPresenter);
}
