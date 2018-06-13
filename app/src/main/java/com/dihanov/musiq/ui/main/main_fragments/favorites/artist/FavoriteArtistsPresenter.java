package com.dihanov.musiq.ui.main.main_fragments.favorites.artist;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.ArtistDetailsIntentShowableImpl;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.interfaces.RecyclerViewExposable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.main.MainContract;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class FavoriteArtistsPresenter extends ArtistDetailsIntentShowableImpl implements FavoriteArtistsContract.Presenter {
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
        if (this.compositeDisposable != null) {
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
        //resetting the adapter
        recyclerViewExposable.getRecyclerView().setAdapter(new ArtistAdapter(this.mainActivity, new ArrayList<>(), FavoriteArtistsPresenter.this, true));

        List<Artist> deserializedList = new ArrayList<>();

        for (String serializedArtist : favorites) {
            String actualValue = serializedArtist.split(" \\_\\$\\_ ")[1];
            Artist artist;
            try {
                artist = new Gson().fromJson(actualValue, Artist.class);
            } catch (IllegalStateException e) {
                SpecificArtist specificArtist = new Gson().fromJson(actualValue, SpecificArtist.class);
                artist = specificArtist.getArtist();
            }

            deserializedList.add(artist);
        }

        Single.just(deserializedList)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mainActivity.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Artist> artists) {
                        ((ArtistAdapter) recyclerViewExposable.getRecyclerView().getAdapter()).setArtistList(artists);
                        recyclerViewExposable.getRecyclerView().getAdapter().notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }
}
