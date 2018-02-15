package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlayingFragment;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlayingFragmentContract;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlayingFragmentPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

@Module
public abstract class NowPlayingFragmentModule {
    @Binds
    @PerActivity
    abstract NowPlayingFragmentContract.View provideNowPlayingFragmentView(NowPlayingFragment nowPlayingFragment);

    @Binds
    @PerActivity
    abstract NowPlayingFragmentContract.Presenter provideNowPlayingFragmentPresenter(NowPlayingFragmentPresenter nowPlayingFragmentPresenter);
}
