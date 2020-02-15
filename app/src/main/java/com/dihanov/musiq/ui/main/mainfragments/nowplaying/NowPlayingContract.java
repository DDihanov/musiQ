package com.dihanov.musiq.ui.main.mainfragments.nowplaying;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.interfaces.TrackLovable;
import com.dihanov.musiq.interfaces.TrackUnlovable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.RecentTracksWrapper;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.HashMap;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public interface NowPlayingContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void setRecentTracks(RecentTracksWrapper recentTracksWrapper);

        void showToastTrackLoved();

        void showToastTrackUnloved();

        void startActivityWithExtras(HashMap<String, String> bundleExtra);

        void showAlbumDetails(Album fullAlbum);
    }

    interface Presenter extends BasePresenter<View>, TrackLovable, TrackUnlovable {
        void loadRecentScrobbles();

        void fetchArtist(String artistName);

        void fetchEntireAlbumInfo(String artistName, String albumName);
    }
}
