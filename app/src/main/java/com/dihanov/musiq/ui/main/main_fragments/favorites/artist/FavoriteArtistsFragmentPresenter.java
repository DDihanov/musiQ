package com.dihanov.musiq.ui.main.main_fragments.favorites.artist;

import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.interfaces.ArtistDetailsIntentShowableImpl;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.main.MainActivityContract;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class FavoriteArtistsFragmentPresenter extends ArtistDetailsIntentShowableImpl implements FavoriteArtistsFragmentContract.Presenter{
    private static final long DELAY_IN_MILLIS = 500;
    private static final int limit = 20;

    @Inject LastFmApiClient lastFmApiClient;

    private FavoriteArtistFragment artistResultFragment;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RecyclerView recyclerView;
    private MainActivityContract.View mainActivity;

    @Inject
    public FavoriteArtistsFragmentPresenter() {
    }

    @Override
    public void takeView(FavoriteArtistsFragmentContract.View view) {
        this.artistResultFragment = (FavoriteArtistFragment) view;
        this.recyclerView = view.getRecyclerView();
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
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    this.showArtistDetailsIntent(artistName, mainActivity);
                });
    }

    @Override
    public void loadFavoriteArtists(Set<String> favorites) {

    }
}
