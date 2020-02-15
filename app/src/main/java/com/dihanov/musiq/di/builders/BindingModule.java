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
import com.dihanov.musiq.di.modules.ProfileModule;
import com.dihanov.musiq.di.modules.ProfileUserFriendsModule;
import com.dihanov.musiq.di.modules.ProfileUserInfoModule;
import com.dihanov.musiq.di.modules.UserLovedTracksModule;
import com.dihanov.musiq.di.modules.UserTopArtistsFragmentModule;
import com.dihanov.musiq.di.modules.UserTopTracksFragmentModule;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.detail.detailfragments.ArtistSpecificsAlbum;
import com.dihanov.musiq.ui.detail.detailfragments.ArtistSpecificsBiography;
import com.dihanov.musiq.ui.detail.detailfragments.detailfragmentstoptracks.ArtistSpecificsTopTracks;
import com.dihanov.musiq.ui.login.Login;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.mainfragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.mainfragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.mainfragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.mainfragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.mainfragments.nowplaying.NowPlaying;
import com.dihanov.musiq.ui.main.mainfragments.usertopartists.UserTopArtists;
import com.dihanov.musiq.ui.main.mainfragments.usertoptracks.UserTopTracks;
import com.dihanov.musiq.ui.settings.AppCompatPreferenceActivity;
import com.dihanov.musiq.ui.settings.Settings;
import com.dihanov.musiq.ui.settings.profile.Profile;
import com.dihanov.musiq.ui.settings.profile.userbio.ProfileUserInfo;
import com.dihanov.musiq.ui.settings.profile.userfriendsinfo.ProfileUserFriendsInfo;
import com.dihanov.musiq.ui.settings.profile.userlovedtracks.UserLovedTracksView;
import com.dihanov.musiq.ui.splash.SplashScreen;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class BindingModule {
    @PerActivity
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity bindMainActivity();

    @PerActivity
    @ContributesAndroidInjector(modules = ArtistDetailsActivityModule.class)
    abstract ArtistDetails bindArtistDetailActivity();

    @PerActivity
    @ContributesAndroidInjector()
    abstract AppCompatPreferenceActivity bindPreferenceAnctivity();

    @PerActivity
    @ContributesAndroidInjector()
    abstract SplashScreen bindSplashScreen();

    @PerActivity
    @ContributesAndroidInjector
    abstract Settings bindSettingsActivity();

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

    @PerActivity
    @ContributesAndroidInjector(modules = ProfileModule.class)
    abstract Profile bindProfileModule();

    @PerActivity
    @ContributesAndroidInjector(modules = ProfileUserInfoModule.class)
    abstract ProfileUserInfo bindProfileUserInfo();

    @PerActivity
    @ContributesAndroidInjector(modules = ProfileUserFriendsModule.class)
    abstract ProfileUserFriendsInfo bindProfileUserFriendsInfo();

    @PerActivity
    @ContributesAndroidInjector(modules = UserLovedTracksModule.class)
    abstract UserLovedTracksView bindUserLovedTracks();

    @PerActivity
    @ContributesAndroidInjector
    abstract Settings.ScrobbleReviewFragment bindReviewFragment();

    @PerActivity
    @ContributesAndroidInjector
    abstract Settings.PlayerPreferenceFragment bindPlayerPreferenceFragment();

    @PerActivity
    @ContributesAndroidInjector
    abstract Settings.NotificationPreferenceFragment bindNotificationPreferenceFragment();
}
