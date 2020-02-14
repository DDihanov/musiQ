package com.dihanov.musiq.ui.main.main_fragments.user_top_artists;

import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public interface UserTopArtistsContract {
    interface View extends BaseView<UserTopArtistsContract.Presenter> {
        void configureBarChart(UserTopArtists userTopArtists);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<UserTopArtistsContract.View>{
        void loadTopArtists(String timeframe);
    }
}
