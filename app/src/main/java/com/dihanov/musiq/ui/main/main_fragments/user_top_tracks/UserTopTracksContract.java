package com.dihanov.musiq.ui.main.main_fragments.user_top_tracks;

import com.dihanov.musiq.models.UserTopTracks;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public interface UserTopTracksContract {
    interface View extends BaseView<UserTopTracksContract.Presenter> {
        void configureBarChart(UserTopTracks userTopTracksModel);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<UserTopTracksContract.View>{
        void loadTopTracks(String timeframe);
    }
}
