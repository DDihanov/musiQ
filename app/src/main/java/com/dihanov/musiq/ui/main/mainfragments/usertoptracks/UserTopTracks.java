package com.dihanov.musiq.ui.main.mainfragments.usertoptracks;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.mainfragments.ViewPagerCustomizedFragment;
import com.dihanov.musiq.util.BarChartConfigurator;
import com.dihanov.musiq.util.Period;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopTracks extends ViewPagerCustomizedFragment implements UserTopTracksContract.View {
    public static final String TITLE = "your top tracks";
    private static final String BAR_CHART_TITLE = "top track artists";

    @BindView(R.id.user_artist_tracks_chart)
    HorizontalBarChart barChart;

    @BindView(R.id.top_tracks_timeframe_spinner)
    Spinner spinner;

    @Inject
    UserTopTracksContract.Presenter userTopTracksPresenter;

    @Inject
    BarChartConfigurator barChartConfigurator;

    private MainActivity mainActivity;


    public static UserTopTracks newInstance() {
        Bundle args = new Bundle();
        UserTopTracks userTopTracks = new UserTopTracks();
        userTopTracks.setArguments(args);
        return userTopTracks;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_tracks_fragment, container, false);
        ButterKnife.bind(this, view);

        //very important to call this - this enables us to use the below method(onCreateOptionsMenu), and allows us
        //to receive calls from MainActivity's onCreateOptionsMenu
        setHasOptionsMenu(true);
        barChartConfigurator.configureChartColor(barChart);
        barChart.setNoDataText(getString(R.string.NO_DATA_MESSAGE));

        this.userTopTracksPresenter.takeView(this);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userTopTracksPresenter.leaveView();
    }


    @OnItemSelected(R.id.top_tracks_timeframe_spinner)
    public void spinnerItemSelected(Spinner spinner, int position) {
        switch (position) {
            case 0:
                this.userTopTracksPresenter.loadTopTracks(Period.OVERALL);
                return;
            case 1:
                this.userTopTracksPresenter.loadTopTracks(Period.SEVEN_DAY);
                return;
            case 2:
                this.userTopTracksPresenter.loadTopTracks(Period.ONE_MONTH);
                return;
            case 3:
                this.userTopTracksPresenter.loadTopTracks(Period.THREE_MONTH);
                return;
            case 4:
                this.userTopTracksPresenter.loadTopTracks(Period.TWELVE_MONTH);
                return;
        }
    }

    @Override
    public void configureBarChart(com.dihanov.musiq.models.UserTopTracks userTopTracksModel) {
        barChartConfigurator.configureBarChartTrack(userTopTracksModel.getToptracks().getTrack(), TITLE, requireActivity(), barChart);
    }

    @Override
    public void showProgressBar() {
        mainActivity.showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        mainActivity.hideProgressBar();
    }
}
