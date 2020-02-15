package com.dihanov.musiq.ui.detail.detailfragments.detailfragmentstoptracks;

import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistTopTracks;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;

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
    private static final int LIMIT = 10;
    private static final String TAG = ArtistSpecificsTopTracksPresenter.class.getSimpleName();
    private ArtistSpecificsTopTracksContract.View artistTopTracksView;

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArtistSpecificsTopTracksPresenter(LastFmApiClient lastFmApiClient){
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistSpecificsTopTracksContract.View view) {
        this.artistTopTracksView = view;
    }

    @Override
    public void leaveView() {
        this.artistTopTracksView = null;
    }

    @Override
    public void loadArtistTopTracks(Artist artist) {
        lastFmApiClient.getLastFmApiService()
                .getArtistTopTracks(artist.getName(), LIMIT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArtistTopTracks>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        artistTopTracksView.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(ArtistTopTracks artistTopTracks) {
                        artistTopTracksView.configureBarChart(artistTopTracks);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                        artistTopTracksView.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        artistTopTracksView.hideProgressBar();
                    }
                });
    }
}
