package com.dihanov.musiq.ui.main.main_fragments.user_top_tracks;

import android.content.Context;

import com.dihanov.musiq.interfaces.HorizontalBarChartGettable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public interface UserTopTracksContract {
    interface View extends BaseView<UserTopTracksContract.Presenter>, HorizontalBarChartGettable{
        Context getContext();

        MainActivity getMainActivity();

        HorizontalBarChart getHorizontalBarChart();
    }

    interface Presenter extends BasePresenter<UserTopTracksContract.View>{
        void loadTopTracks(UserTopTracksContract.View view, String timeframe);
    }
}
