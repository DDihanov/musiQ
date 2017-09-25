package com.dihanov.musiq.ui.main;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.service.LastFmApiClient;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    MainActivityContract.View mainActivityView;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    public MainActivityPresenter() {
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        this.mainActivityView = null;
    }


    @Override
    public void setBackdropImageChangeListener(MainActivity mainActivity, ImageView backdrop) {
        int counter = 0;
        Observable<List<Artist>> topArtists = lastFmApiClient.getLastFmApiService().chartTopArtists(5)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches());

        topArtists.subscribe(new Observer<List<Artist>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(List<Artist> artists) {
                int counter = 0;
                while(true){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(counter == 5){
                        counter = 0;
                    }
                    String linkUrl = artists.get(counter).getImage().get(2).getText();
                    Glide.with(mainActivity).load(linkUrl).crossFade(1000).into(backdrop);
                    counter++;
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
}
