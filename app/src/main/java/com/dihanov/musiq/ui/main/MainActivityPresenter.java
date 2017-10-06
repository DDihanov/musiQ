package com.dihanov.musiq.ui.main;


import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {
    private static final String LOADING_ARTISTS = "loading this week's top artists...";
    private static final long NETWORK_CHECK_THREAD_TIMEOUT = 5000;
    private static int TOP_ARTIST_LIMIT = 6;

    private MainActivityContract.View mainActivityView;
    private Disposable disposable;
    private RecyclerView recyclerView;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    public MainActivityPresenter() {
    }

    @Override
    public void takeView(MainActivityContract.View view) {
        this.recyclerView = view.getRecyclerView();
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        disposable.dispose();
        this.mainActivityView = null;
    }


    @Override
    public void setBackdropImageChangeListener(MainActivity mainActivity) {
        setListenerOnFoundConnection(mainActivity);
    }

    private void loadBackdrop(MainActivity mainActivity) {
        if (Constants.isTablet(mainActivity) && Constants.getOrientation(mainActivity) == Configuration.ORIENTATION_LANDSCAPE) {
            TOP_ARTIST_LIMIT = 10;
        }

        lastFmApiClient.getLastFmApiService().chartTopArtists(TOP_ARTIST_LIMIT)
//                .flatMapIterable(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
//                .delay(3000L, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Constants.showTooltip(mainActivity, mainActivity.getBirdIcon(), LOADING_ARTISTS);
                        mainActivity.showProgressBar();
                        disposable = d;
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        TopArtistAdapter topArtistAdapter = new TopArtistAdapter(mainActivity, (ArrayList<Artist>) artists);
                        recyclerView.setAdapter(topArtistAdapter);
                        recyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView.smoothScrollToPosition(artists.size() - 1);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        mainActivity.hideProgressBar();
                    }
                });
    }

    private void setListenerOnFoundConnection(MainActivity mainActivity) {
        Thread newConnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Connectivity.isConnected(mainActivity)) {
                    try {
                        Constants.showNetworkErrorTooltip(mainActivity);
                        Thread.sleep(NETWORK_CHECK_THREAD_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBackdrop(mainActivity);
                    }
                });
            }
        });

        if (!newConnThread.isAlive()) {
            newConnThread.start();
        }
    }


    //unused method to cycle through images
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
