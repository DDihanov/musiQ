package com.dihanov.musiq.ui.settings.profile.user_loved_tracks;

import com.dihanov.musiq.interfaces.ToastShowable;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;

public interface UserLovedTracksContract {
    interface View extends BaseView<Presenter>, ToastShowable {
        void loadLovedTracks(List<Track> lovedTracks);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View>{
        void fetchLovedTracks(int limit);

        void unloveTrack(String artistName, String trackName);
    }
}

