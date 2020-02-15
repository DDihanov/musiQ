package com.dihanov.musiq.ui.main.mainfragments.usertopartists;

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

public class UserTopArtists extends ViewPagerCustomizedFragment implements UserTopArtistsContract.View {
    public static final String TITLE = "your top artists";

    @BindView(R.id.user_top_artists_chart)
    HorizontalBarChart barChart;

    @BindView(R.id.top_artists_timeframe_spinner)
    Spinner timeFrameSpinner;

    @Inject
    UserTopArtistsContract.Presenter userTopArtistsPresenter;

    @Inject
    BarChartConfigurator barChartConfigurator;

    private MainActivity mainActivity;


    public static UserTopArtists newInstance() {
        Bundle args = new Bundle();
        UserTopArtists userTopArtists = new UserTopArtists();
        userTopArtists.setArguments(args);
        return userTopArtists;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_artists_fragment, container, false);
        ButterKnife.bind(this, view);

        //very important to call this - this enables us to use the below method(onCreateOptionsMenu), and allows us
        //to receive calls from MainActivity's onCreateOptionsMenu
        setHasOptionsMenu(true);
        barChartConfigurator.configureChartColor(barChart);
        barChart.setNoDataText(getString(R.string.NO_DATA_MESSAGE));


        this.userTopArtistsPresenter.takeView(this);
        return view;
    }

    @OnItemSelected(R.id.top_artists_timeframe_spinner)
    public void spinnerItemSelected(Spinner spinner, int position) {
        switch (position) {
            case 0:
                this.userTopArtistsPresenter.loadTopArtists(Period.OVERALL);
                return;
            case 1:
                this.userTopArtistsPresenter.loadTopArtists(Period.SEVEN_DAY);
                return;
            case 2:
                this.userTopArtistsPresenter.loadTopArtists(Period.ONE_MONTH);
                return;
            case 3:
                this.userTopArtistsPresenter.loadTopArtists(Period.THREE_MONTH);
                return;
            case 4:
                this.userTopArtistsPresenter.loadTopArtists(Period.TWELVE_MONTH);
                return;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userTopArtistsPresenter.leaveView();
    }

    @Override
    public void configureBarChart(com.dihanov.musiq.models.UserTopArtists userTopArtists) {
        barChartConfigurator.configureBarChartArtist(userTopArtists.getTopartists().getArtist(), TITLE, requireActivity(), barChart);
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
