package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.content.Context;

import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public interface NowPlayingContract {
    interface View extends BaseView<Presenter>, RecyclerViewExposable{
        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void loveTrack(Scrobble scrobble);

        void loadRecentScrobbles(NowPlayingContract.View nowPlayingFragment);
    }
}
