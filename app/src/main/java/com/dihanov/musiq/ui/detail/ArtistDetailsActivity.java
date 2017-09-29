package com.dihanov.musiq.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsActivity extends DaggerAppCompatActivity implements ArtistDetailsActivityContract.View{
    @Inject ArtistDetailsActivityPresenter presenter;

    private Artist artist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent receiveIntent = getIntent();
        artist = new Gson().fromJson(receiveIntent.getStringExtra(Constants.ARTIST), Artist.class);

        this.presenter.takeView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.leaveView();
    }
}
