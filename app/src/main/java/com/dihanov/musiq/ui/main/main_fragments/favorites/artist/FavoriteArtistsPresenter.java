package com.dihanov.musiq.ui.main.main_fragments.favorites.artist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dihanov.musiq.interfaces.ArtistDetailsIntentShowableImpl;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.main.MainContract;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class FavoriteArtistsPresenter extends ArtistDetailsIntentShowableImpl implements FavoriteArtistsContract.Presenter{
    private static final long DELAY_IN_MILLIS = 500;

    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private FavoriteArtistsContract.View artistResultFragment;
    private MainContract.View mainActivity;

    @Inject
    public FavoriteArtistsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(FavoriteArtistsContract.View view) {
        this.artistResultFragment = view;
        this.mainActivity = artistResultFragment.getMainActivity();
    }

    @Override
    public void leaveView() {
        if(this.compositeDisposable != null){
            compositeDisposable.clear();
        }
        this.artistResultFragment = null;
    }

    @Override
    public void addOnArtistResultClickedListener(ClickableArtistViewHolder viewHolder, String artistName) {
        RxView.clicks(viewHolder.getThumbnail())
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    this.showArtistDetailsIntent(artistName, mainActivity);
                });
    }


    @Override
    public void loadFavoriteArtists(Set<String> favorites, RecyclerViewExposable recyclerViewExposable) {
        RecyclerView recyclerView = recyclerViewExposable.getRecyclerView();
        //resetting the adapter
        recyclerView.setAdapter(new ArtistAdapter((Context) this.mainActivity, new ArrayList<>(), FavoriteArtistsPresenter.this));


        List<Observable<SpecificArtist>> observables = new ArrayList<>();

        for (String favorite : favorites) {
            observables.add(lastFmApiClient.getLastFmApiService().getSpecificArtistInfo(favorite).subscribeOn(Schedulers.io()));
        }

        Observable.zip(observables, objects -> {
            List<Artist> result = new ArrayList<>();
            for (Object object : objects) {
                result.add(((SpecificArtist) object).getArtist());
            }
            return result;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainActivity.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Artist> generalArtistSearch) {
                        for (Artist artistSearch : generalArtistSearch) {
                            ((ArtistAdapter) recyclerView.getAdapter()).addArtist(artistSearch);
                        }
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(this.getClass().getSimpleName(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }
}
