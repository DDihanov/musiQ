package com.dihanov.musiq.ui.main.main_fragments.now_playing;

import android.content.Context;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.interfaces.SpecificAlbumClickable;
import com.dihanov.musiq.interfaces.SpecificArtistSearchable;
import com.dihanov.musiq.interfaces.ToastShowable;
import com.dihanov.musiq.service.scrobble.Scrobble;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public interface NowPlayingContract {
    interface View extends BaseView<Presenter>, RecyclerViewExposable, ToastShowable, MainViewFunctionable {
        Context getContext();

        MainActivity getMainActivity();

        void showToast(Context context, String message);
    }

    interface Presenter extends BasePresenter<View>, SpecificAlbumClickable, SpecificArtistSearchable {
        void loveTrack(Scrobble scrobble);

        void loadRecentScrobbles(NowPlayingContract.View nowPlayingFragment);
    }
}
