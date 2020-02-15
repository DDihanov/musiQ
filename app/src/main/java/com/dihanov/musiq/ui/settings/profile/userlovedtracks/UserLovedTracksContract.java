package com.dihanov.musiq.ui.settings.profile.userlovedtracks;

import com.dihanov.musiq.interfaces.TrackLovable;
import com.dihanov.musiq.interfaces.TrackUnlovable;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;

public interface UserLovedTracksContract {
    interface View extends BaseView<Presenter> {
        void loadLovedTracks(List<Track> lovedTracks);

        void showProgressBar();

        void hideProgressBar();

        void showToastTrackLoved();

        void showToastTrackUnloved();
    }

    interface Presenter extends BasePresenter<View>, TrackUnlovable, TrackLovable {
        void fetchLovedTracks(int limit);
    }
}

