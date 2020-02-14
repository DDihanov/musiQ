package com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.models.Track;
import com.dihanov.musiq.ui.detail.ArtistDetails;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public class ArtistSpecificsTopTracks extends DaggerFragment implements ArtistSpecificsTopTracksContract.View {
    public static final String TITLE = "artist top tracks";
    private static final String BAR_CHART_TITLE = "аrtist top tracks";
    public static final int CONST = 65;
    public static final int TEXT_SIZE_CONST = 30;

    @Inject ArtistSpecificsTopTracksContract.Presenter presenter;

    @BindView(R.id.detail_user_artist_tracks_chart)
    HorizontalBarChart barChart;

    private ArtistDetails artistDetailsActivity;

    public ArtistSpecificsTopTracks(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetails) context;
    }

    public static ArtistSpecificsTopTracks newInstance(){
        Bundle args = new Bundle();
        ArtistSpecificsTopTracks artistSpecificsTopTracks = new ArtistSpecificsTopTracks();
        artistSpecificsTopTracks.setArguments(args);
        return artistSpecificsTopTracks;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_top_tracks_fragment, container, false);
        ButterKnife.bind(this, view);

        configureChart();

        this.presenter.takeView(this);
        Artist artist = artistDetailsActivity.getArtist();
        if(artist != null && artist.getName() != null){
            this.presenter.loadArtistTopTracks(artist);
        }

        return view;
    }

    private void configureChart() {
        barChart.setNoDataTextColor(R.color.colorPrimary);
    }

    @Override
    public void configureBarChart(ArtistTopTracks artistTopTracks) {
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

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(false);
        barData.setDrawValues(false);
        barChart.setData(barData);
        barChart.getXAxis().setLabelCount(labels.size() - 1);
        barChart.getXAxis().setTextSize(HelperMethods.getScreenWidth(requireActivity()) / TEXT_SIZE_CONST);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setTypeface(HelperMethods.createTypefaceFromFont(requireActivity(), "fonts/cabin_regular.ttf"));
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            if((int) value > labels.size()){
                return "";
            }
            return labels.get((int)value);
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(requireActivity(), (int) e.getY() + " listens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                return;
            }
        });
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setXOffset(-HelperMethods.getScreenWidth(requireActivity()) + CONST);
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

    @Override
    public void showProgressBar() {
        ((ArtistDetails) requireActivity()).showProgressBar();
    }

    @Override
    public void hideProgressBar() {
        ((ArtistDetails) requireActivity()).hideProgressBar();
    }
}
