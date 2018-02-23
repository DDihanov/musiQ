package com.dihanov.musiq.ui.main.main_fragments.user_top_artists;

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

public class UserTopArtists extends DaggerFragment implements UserTopArtistsContract.View{
    public static final String TITLE = "your top artists";

    @BindView(R.id.user_top_artists_chart)
    HorizontalBarChart topArtistsChart;

    @BindView(R.id.top_artists_timeframe_spinner)
    Spinner timeFrameSpinner;

    @Inject
    UserTopArtistsContract.Presenter userTopArtistsPresenter;

    private MainActivity mainActivity;


    public static UserTopArtists newInstance() {
        Bundle args = new Bundle();
        UserTopArtists userTopArtists = new UserTopArtists();
        userTopArtists.setArguments(args);
        return userTopArtists;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public HorizontalBarChart getTopArtistsBarChart() {
        return this.topArtistsChart;
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
        configureChart();


        this.userTopArtistsPresenter.takeView(this);
        return view;
    }


    private void configureChart() {
        topArtistsChart.setNoDataTextColor(R.color.colorPrimary);
        topArtistsChart.setNoDataText(getString(R.string.NO_DATA_MESSAGE));
    }

    @OnItemSelected(R.id.top_artists_timeframe_spinner)
    public void spinnerItemSelected(Spinner spinner, int position){
        switch(position){
            case 0:
                this.userTopArtistsPresenter.loadTopArtists(this, Period.OVERALL);
                return;
            case 1:
                this.userTopArtistsPresenter.loadTopArtists(this, Period.SEVEN_DAY);
                return;
            case 2:
                this.userTopArtistsPresenter.loadTopArtists(this, Period.ONE_MONTH);
                return;
            case 3:
                this.userTopArtistsPresenter.loadTopArtists(this, Period.THREE_MONTH);
                return;
            case 4:
                this.userTopArtistsPresenter.loadTopArtists(this, Period.TWELVE_MONTH);
                return;
        }
    }

    @Override
    public Context getContext() {
        return this.mainActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userTopArtistsPresenter.leaveView();
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
