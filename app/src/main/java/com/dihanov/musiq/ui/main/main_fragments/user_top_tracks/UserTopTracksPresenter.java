package com.dihanov.musiq.ui.main.main_fragments.user_top_tracks;

import android.graphics.Typeface;
import android.util.Log;
import android.widget.Toast;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.models.UserTopTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopTracksPresenter implements UserTopTracksContract.Presenter {
    private static final String BAR_CHART_TITLE = "top track artists";
    private static final int LIMIT = 10;
    public static final int CONST = 65;
    public static final int TEXT_SIZE_CONST = 30;

    private final LastFmApiClient lastFmApiClient;

    private UserTopTracksContract.View userTopTracks;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MainContract.View mainActivity;

    @Inject
    public UserTopTracksPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(UserTopTracksContract.View view) {
        this.userTopTracks = view;
        this.mainActivity = userTopTracks.getMainActivity();
    }


    @Override
    public void leaveView() {
        if (this.compositeDisposable != null) {
            compositeDisposable.clear();
        }
        this.userTopTracks = null;
    }

    @Override
    public void loadTopTracks(UserTopTracksContract.View view, String timeframe) {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        if (username.isEmpty() || username.equals("")) return;

        lastFmApiClient.getLastFmApiService()
                .getUserTopTracks(username, LIMIT, timeframe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTopTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        mainActivity.showProgressBar();
                    }

                    @Override
                    public void onNext(UserTopTracks userTopTracks) {
                        List<BarEntry> entries = new ArrayList<>();
                        List<String> labels = new ArrayList<>();
                        List<Track> tracks = userTopTracks.getToptracks().getTrack();
                        int counter = 0;


                        for (int i = tracks.size() - 1; i >= 0; i--) {
                            Track track = tracks.get(i);
                            float playcount = Float.parseFloat(track.getPlaycount());
                            entries.add(new BarEntry(counter, playcount >= 0 ? playcount : 0f, track.getName()));
                            labels.add(track.getName());
                            counter++;
                        }


                        BarDataSet barDataSet = new BarDataSet(entries, BAR_CHART_TITLE);
                        BarData barData = new BarData(barDataSet);
                        HorizontalBarChart barChart = view.getTrackBarChart();

                        configureBarChart(labels, barDataSet, barData, barChart);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(UserTopTracksPresenter.class.getSimpleName(), e.getMessage());
                        compositeDisposable.clear();
                        mainActivity.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        mainActivity.hideProgressBar();
                    }
                });
    }

    private void configureBarChart(List<String> labels, BarDataSet barDataSet, BarData barData, HorizontalBarChart barChart) {
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(false);
        barData.setDrawValues(false);
        barChart.setData(barData);
        barChart.getXAxis().setLabelCount(labels.size() - 1);
        barChart.getXAxis().setTextSize(HelperMethods.getScreenWidth(mainActivity.getMainActivity()) / TEXT_SIZE_CONST);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setTypeface(Typeface.createFromAsset(mainActivity.getContext().getAssets(), "fonts/cabin_regular.ttf"));
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            if((int) value > labels.size()){
                return "";
            }
            return labels.get((int)value);
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(mainActivity.getContext(), String.valueOf((int)e.getY()) + " listens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                return;
            }
        });
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setXOffset(-HelperMethods.getScreenWidth(mainActivity.getMainActivity()) + CONST);
        barChart.setDrawValueAboveBar(false);
        barChart.getViewPortHandler().fitScreen();
        barChart.setScaleEnabled(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(true);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);
        barChart.animateY(500);
        barChart.invalidate();
    }
}
