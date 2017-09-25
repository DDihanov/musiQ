package com.dihanov.musiq.ui.main;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.service.LastFmApiClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
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
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        lastFmApiClient.getLastFmApiService().chartTopArtists(5)
                .flatMapIterable(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .doOnNext(artist -> Observable.timer(3000L, TimeUnit.MILLISECONDS))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Artist>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Artist artist) {
                        String url = artist.getImage().get(3).getText();
                        Glide.with(mainActivity).load(url).crossFade().into(backdrop);
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
