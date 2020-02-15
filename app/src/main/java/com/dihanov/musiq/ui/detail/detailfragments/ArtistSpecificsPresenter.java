package com.dihanov.musiq.ui.detail.detailfragments;

import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.SpecificAlbum;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class ArtistSpecificsPresenter implements ArtistSpecificsContract.Presenter {
    private final LastFmApiClient lastFmApiClient;

    private ArtistSpecificsContract.View artistDetailsFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArtistSpecificsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ArtistSpecificsContract.View view) {
        this.artistDetailsFragment = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsFragment = null;
        compositeDisposable.clear();
    }


    @Override
    public void fetchEntireAlbumInfo(String artistName, String albumName) {
        lastFmApiClient.getLastFmApiService()
                .searchForSpecificAlbum(artistName, albumName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(specificAlbum -> specificAlbum)
                .subscribe(new Observer<SpecificAlbum>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        artistDetailsFragment.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(SpecificAlbum specificAlbum) {
                        Album fullAlbum = specificAlbum.getAlbum();
                        artistDetailsFragment.showAlbumDetails(fullAlbum);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        artistDetailsFragment.hideProgressBar();
                    }
                });
    }
}
