package com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsContract;
import com.dihanov.musiq.util.AppLog;
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
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public class ArtistSpecificsTopTracksPresenter implements ArtistSpecificsTopTracksContract.Presenter {
    public static final int LIMIT = 10;
    private static final String BAR_CHART_TITLE = "аrtist top tracks";
    private static final String TAG = ArtistSpecificsTopTracksPresenter.class.getSimpleName();
    public static final int CONST = 65;
    public static final int TEXT_SIZE_CONST = 30;
    private ArtistSpecificsTopTracksContract.View artistTopTracks;
    private ArtistDetailsContract.View detailView;

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private Artist artist;


    @Inject
    public ArtistSpecificsTopTracksPresenter(LastFmApiClient lastFmApiClient){
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistSpecificsTopTracksContract.View view) {
        this.artistTopTracks = view;
        this.detailView = view.getDetailActivity();
    }

    @Override
    public void leaveView() {
        this.artistTopTracks = null;
    }

    @Override
    public void loadArtistTopTracks(ArtistSpecificsTopTracksContract.View view) {

        if(artist == null && artist.getName() == null){
            return;
        }

        lastFmApiClient.getLastFmApiService()
                .getArtistTopTracks(artist.getName(), LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistTopTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        detailView.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArtistTopTracks artistTopTracks) {
                        List<BarEntry> entries = new ArrayList<>();
                        List<String> labels = new ArrayList<>();
                        List<Track> tracks = artistTopTracks.getToptracks().getTrack();

                        if(tracks == null || tracks.isEmpty()) return;

                        Collections.sort(tracks,
                                (o1, o2) -> Integer.valueOf(o2.getPlaycount()).compareTo(Integer.valueOf(o1.getPlaycount())));

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
                        HorizontalBarChart barChart = view.getHorizontalBarChart();

                        configureBarChart(labels, barDataSet, barData, barChart);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                        detailView.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        detailView.hideProgressBar();
                    }
                });
    }

    @Override
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    private void configureBarChart(List<String> labels, BarDataSet barDataSet, BarData barData, HorizontalBarChart barChart) {
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(false);
        barData.setDrawValues(false);
        barChart.setData(barData);
        barChart.getXAxis().setLabelCount(labels.size() - 1);
        barChart.getXAxis().setTextSize(HelperMethods.getScreenWidth(detailView.getDetailActivity()) / TEXT_SIZE_CONST);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setTypeface(HelperMethods.createTypefaceFromFont(detailView.getDetailActivity(), "fonts/cabin_regular.ttf"));
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            if((int) value > labels.size()){
                return "";
            }
            return labels.get((int)value);
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                detailView.showToast(detailView.getDetailActivity(), String.valueOf((int)e.getY()) + " listens");
            }

            @Override
            public void onNothingSelected() {
                return;
            }
        });
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setXOffset(-HelperMethods.getScreenWidth(detailView.getDetailActivity()) + CONST);
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
