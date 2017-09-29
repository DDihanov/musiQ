package com.dihanov.musiq.ui.main;


import android.widget.GridView;
import android.widget.ImageView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.service.LastFmApiClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    MainActivityContract.View mainActivityView;

    private Disposable disposable;

    private GridView gridView;

    @Inject LastFmApiClient lastFmApiClient;

    @Inject
    public MainActivityPresenter() {
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.gridView = view.getGridView();
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        disposable.dispose();
        this.mainActivityView = null;
    }


    @Override
    public void setBackdropImageChangeListener(MainActivity mainActivity) {
        lastFmApiClient.getLastFmApiService().chartTopArtists(6)
//                .flatMapIterable(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .delay(3000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        TopArtistAdapter topArtistAdapter = new TopArtistAdapter(mainActivity, R.layout.top_artist_viewholder, (ArrayList<Artist>) artists);
                        gridView.setAdapter(topArtistAdapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

//
//        initBackdropImageChanger(mainActivity, backdrop, artists);
    }

    private void initBackdropImageChanger(MainActivity mainActivity, ImageView backdrop, List<Artist> artists) {
//        Handler handler = new Handler();
//        //this method checks the artist list, and if it's not empty, it loads the images onto the backdrop
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while(true){
//                    if(!artists.isEmpty()){
//                        for (Artist artist : artists) {
//                            try {
//                                Thread.sleep(5000);
//                                String url = artist.getImage().get(4).getText();
//
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Glide.with(mainActivity).load(url).asBitmap()
//                                                .format(DecodeFormat.PREFER_ARGB_8888)
//                                                .crossFade(1000).into(backdrop);
//                                    }
//                                });
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }).start();
    }
}
