package com.dihanov.musiq.di.builders;

import com.dihanov.musiq.di.annotations.PerActivity;
import com.dihanov.musiq.di.modules.AlbumResultFragmentModule;
import com.dihanov.musiq.di.modules.ArtistDetailsActivityModule;
import com.dihanov.musiq.di.modules.ArtistDetailsFragmentModule;
import com.dihanov.musiq.di.modules.ArtistResultFragmentModule;
import com.dihanov.musiq.di.modules.ArtistSpecificsTopTracksModule;
import com.dihanov.musiq.di.modules.FavoriteFragmentModule;
import com.dihanov.musiq.di.modules.LoginActivityModule;
import com.dihanov.musiq.di.modules.MainActivityModule;
import com.dihanov.musiq.di.modules.MediaPlayerControlServiceModule;
import com.dihanov.musiq.di.modules.NowPlayingFragmentModule;
import com.dihanov.musiq.di.modules.UserTopArtistsFragmentModule;
import com.dihanov.musiq.di.modules.UserTopTracksFragmentModule;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistSpecificsAlbum;
import com.dihanov.musiq.ui.detail.detail_fragments.ArtistSpecificsBiography;
import com.dihanov.musiq.ui.detail.detail_fragments.detail_fragments_top_tracks.ArtistSpecificsTopTracks;
import com.dihanov.musiq.ui.login.Login;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlaying;
import com.dihanov.musiq.ui.main.main_fragments.user_top_artists.UserTopArtists;
import com.dihanov.musiq.ui.main.main_fragments.user_top_tracks.UserTopTracks;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class ActivityBindingModule {
    @PerActivity
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsActivityModule.class)
    abstract ArtistDetails bindArtistDetailActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistResultFragmentModule.class)
    abstract ArtistResult bindArtistResultFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = AlbumResultFragmentModule.class)
    abstract AlbumResult bindAlbumResultFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsFragmentModule.class)
    abstract ArtistSpecificsBiography bindArtistBiographyFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsFragmentModule.class)
    abstract ArtistSpecificsAlbum bindArtistDetailsAlbumsFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = FavoriteFragmentModule.class)
    abstract FavoriteAlbums bindFavoriteAlbumsFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = FavoriteFragmentModule.class)
    abstract FavoriteArtist bindFavoriteArtistFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = LoginActivityModule.class)
    abstract Login bindLoginActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = NowPlayingFragmentModule.class)
    abstract NowPlaying bindNowPlayingFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = MediaPlayerControlServiceModule.class)
    abstract MediaControllerListenerService bindMediaPlayerControlService();

    @PerActivity
    @ContributesAndroidInjector(modules = UserTopTracksFragmentModule.class)
    abstract UserTopTracks bindUserTopTracksFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = UserTopArtistsFragmentModule.class)
    abstract UserTopArtists bindUserTopArtistsFragment();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistSpecificsTopTracksModule.class)
    abstract ArtistSpecificsTopTracks bindsArtistTopTracksFragment();
}
