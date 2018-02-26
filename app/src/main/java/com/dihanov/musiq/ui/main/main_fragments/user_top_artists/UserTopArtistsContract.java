package com.dihanov.musiq.ui.main.main_fragments.user_top_artists;

import android.content.Context;

import com.dihanov.musiq.interfaces.ToastShowable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;
import com.github.mikephil.charting.charts.HorizontalBarChart;

/**
 * Created by dimitar.dihanov on 2/22/2018.
 */

public interface UserTopArtistsContract {
    interface View extends BaseView<UserTopArtistsContract.Presenter>, ToastShowable{
        Context getContext();

        MainActivity getMainActivity();

        HorizontalBarChart getTopArtistsBarChart();

        void showToast(Context context, String message);
    }

    interface Presenter extends BasePresenter<UserTopArtistsContract.View>{
        void loadTopArtists(UserTopArtistsContract.View view, String timeframe);
    }
}
