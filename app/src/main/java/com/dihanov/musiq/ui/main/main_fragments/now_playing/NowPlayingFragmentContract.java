package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public interface NowPlayingFragmentContract {
    interface View extends BaseView<Presenter>{
        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void loveTrack(Scrobble scrobble);

        void loadRecentScrobbles(RecyclerView recyclerView, NowPlayingFragment nowPlayingFragment);
    }
}
