package com.dihanov.musiq.ui.main.main_fragments.user_top_tracks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.KeyboardHelper;
import com.dihanov.musiq.util.Period;
import com.github.mikephil.charting.charts.HorizontalBarChart;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopTracks extends DaggerFragment implements UserTopTracksContract.View{
    public static final String TITLE = "your top tracks";

    @BindView(R.id.user_artist_tracks_chart)
    HorizontalBarChart topTracksChart;

    @BindView(R.id.top_tracks_timeframe_spinner)
    Spinner spinner;

    @Inject
    UserTopTracksContract.Presenter userTopTracksPresenter;

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
    public HorizontalBarChart getTrackBarChart() {
        return this.topTracksChart;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.top_tracks_fragment, container, false);
        ButterKnife.bind(this, view);

        //very important to call this - this enables us to use the below method(onCreateOptionsMenu), and allows us
        //to receive calls from MainActivity's onCreateOptionsMenu
        setHasOptionsMenu(true);
        configureChart();

        this.userTopTracksPresenter.takeView(this);
        return view;
    }

    private void configureChart() {
        topTracksChart.setNoDataTextColor(R.color.colorPrimary);
        topTracksChart.setNoDataText(getString(R.string.NO_DATA_MESSAGE));
    }


    @Override
    public Context getContext() {
        return this.mainActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userTopTracksPresenter.leaveView();
    }


    @OnItemSelected(R.id.top_tracks_timeframe_spinner)
    public void spinnerItemSelected(Spinner spinner, int position){
        switch(position){
            case 0:
                this.userTopTracksPresenter.loadTopTracks(this, Period.OVERALL);
                return;
            case 1:
                this.userTopTracksPresenter.loadTopTracks(this, Period.SEVEN_DAY);
                return;
            case 2:
                this.userTopTracksPresenter.loadTopTracks(this, Period.ONE_MONTH);
                return;
            case 3:
                this.userTopTracksPresenter.loadTopTracks(this, Period.THREE_MONTH);
                return;
            case 4:
                this.userTopTracksPresenter.loadTopTracks(this, Period.TWELVE_MONTH);
                return;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.getItem(0);
        SearchView search = (SearchView)item.getActionView();
        search.setIconified(true);
        KeyboardHelper.hideKeyboard(getActivity());
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setViewPagerSelection(Constants.ALBUM_POSITION);
                KeyboardHelper.hideKeyboard(mainActivity);
            }
        });
    }
}
