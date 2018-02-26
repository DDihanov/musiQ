package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks.ArtistSpecificsTopTracks;
import com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks.ArtistSpecificsTopTracksContract;
import com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks.ArtistSpecificsTopTracksPresenter;

import dagger.Binds;
import dagger.Module;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

@Module
public abstract class ArtistSpecificsTopTracksModule {
    @Binds
    @PerActivity
    abstract ArtistSpecificsTopTracksContract.View provideView(ArtistSpecificsTopTracks artistSpecificsTopTracks);

    @Binds
    @PerActivity
    abstract ArtistSpecificsTopTracksContract.Presenter providePresenter(ArtistSpecificsTopTracksPresenter artistSpecificsTopTracksPresenter);
}
