package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class NowPlayingFragmentPresenter implements NowPlayingFragmentContract.Presenter {
    private NowPlayingFragment nowPlayingFragment;

    @Inject
    public NowPlayingFragmentPresenter() {
    }


    @Override
    public void takeView(NowPlayingFragmentContract.View view) {
        this.nowPlayingFragment = (NowPlayingFragment) view;
    }

    @Override
    public void leaveView() {
        this.nowPlayingFragment = null;
    }
}
