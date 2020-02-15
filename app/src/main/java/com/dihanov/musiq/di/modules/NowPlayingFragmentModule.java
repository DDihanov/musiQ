package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.mainfragments.nowplaying.NowPlaying;
import com.dihanov.musiq.ui.main.mainfragments.nowplaying.NowPlayingContract;
import com.dihanov.musiq.ui.main.mainfragments.nowplaying.NowPlayingPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

@Module
public abstract class NowPlayingFragmentModule {
    @Binds
    @PerActivity
    abstract NowPlayingContract.View provideNowPlayingFragmentView(NowPlaying nowPlaying);

    @Binds
    @PerActivity
    abstract NowPlayingContract.Presenter provideNowPlayingFragmentPresenter(NowPlayingPresenter nowPlayingPresenter);
}
