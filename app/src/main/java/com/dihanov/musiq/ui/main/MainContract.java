package com.dihanov.musiq.ui.main;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

public interface MainContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void hideKeyboard();
        void setUserInfo(String profilePicUrl, String playcount, String username);
        void setTopArtists(List<Artist> artist);
        void startActivityWithExtras(HashMap<String, String> bundleExtra);
    }

    interface Presenter extends BasePresenter<View> {
        void getUserInfo();

        void loadUserTopArtists(String timeframe);

        void loadChartTopArtists(int limit);

        void fetchArtist(String artistName);
    }
}
