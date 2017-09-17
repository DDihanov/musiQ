package com.dihanov.musiq.ui.main;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import dagger.android.AndroidInjection;
import dagger.android.DaggerActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerActivity implements MainActivityContract.View {
    @Inject
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.search)
    AutoCompleteTextView autoCompleteTextView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private static final List<String> searchData = new LinkedList<>();

    private ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.setAdapter();
        this.mainActivityPresenter.setSearchListener(autoCompleteTextView);
    }


    @OnTextChanged(R.id.search)
    @Override
    public void search() {
        this.mainActivityPresenter.addDataToSearchList(this.autoCompleteTextView.getEditableText().toString().toString(), this.searchData);
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void refreshSearchBar() {
        this.adapter.notifyDataSetChanged();
        this.autoCompleteTextView.showDropDown();
    }

    public void setAdapter() {
        this.adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, searchData);
        this.adapter.setNotifyOnChange(true);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setThreshold(0);
    }

    //    public void getArtistExample() {
//        Single<Artist> artistInfo = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfo("Cher");
////        Call<Artist> call = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfoCall("Cher");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        call.enqueue(new Callback<Artist>() {
////            @Override
////            public void onResponse(Call<Artist> call, Response<Artist> response) {
////                Artist artist = response.body();
////                builder.setTitle(artist.getArtist().getName());
////                builder.setMessage(artist.getArtist().getBio().getContent());
////                builder.show();
////                System.out.println();
////            }
////
////            @Override
////            public void onFailure(Call<Artist> call, Throwable t) {
////            }
////        });
//
////        artistInfo
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(artist -> {
////                            builder.setTitle(artist.getArtist().getName());
////                            builder.setMessage(artist.getArtist().getBio().getSummary());
////                            builder.show();});
////    }
}
