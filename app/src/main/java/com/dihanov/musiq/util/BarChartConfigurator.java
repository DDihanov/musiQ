package com.dihanov.musiq.util;

import android.app.Activity;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.Track;
import com.github.mikephil.charting.charts.BarChart;
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

public class BarChartConfigurator {
    private static final int CONST = 65;
    private static final int TEXT_SIZE_CONST = 30;

    @Inject
    public BarChartConfigurator() {
    }

    public void configureChartColor(BarChart barChart) {
        barChart.setNoDataTextColor(R.color.colorPrimary);
    }

    public void configureBarChartTrack(List<Track> tracks, String title, Activity activity, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

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


        additionalConfig(activity, barChart, entries, labels, title);
    }

    private void additionalConfig(Activity activity, BarChart barChart, List<BarEntry> entries, List<String> labels, String title) {
        BarDataSet barDataSet = new BarDataSet(entries, title);
        BarData barData = new BarData(barDataSet);

        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setDrawValues(false);
        barData.setDrawValues(false);
        barChart.setData(barData);
        barChart.getXAxis().setLabelCount(labels.size() - 1);
        barChart.getXAxis().setTextSize(HelperMethods.getScreenWidth(activity) / TEXT_SIZE_CONST);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getXAxis().setCenterAxisLabels(false);
        barChart.getXAxis().setTypeface(HelperMethods.createTypefaceFromFont(activity, "fonts/cabin_regular.ttf"));
        barChart.getXAxis().setValueFormatter((value, axis) -> {
            if ((int) value > labels.size()) {
                return "";
            }
            return labels.get((int) value);
        });
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Toast.makeText(activity, (int) e.getY() + " listens", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {
                return;
            }
        });
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setXOffset(-HelperMethods.getScreenWidth(activity) + CONST);
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

    public void configureBarChartArtist(List<Artist> artists, String title, Activity activity, BarChart barChart) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        int counter = 0;

        for (int i = artists.size() - 1; i >= 0; i--) {
            Artist artist = artists.get(i);
            float playcount = Float.parseFloat(artist.getPlaycount());
            entries.add(new BarEntry(counter, playcount >= 0 ? playcount : 0f, artist.getName()));
            labels.add(artist.getName());
            counter++;
        }

        additionalConfig(activity, barChart, entries, labels, title);
    }
}
