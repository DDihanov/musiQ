package com.dihanov.musiq.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.main.mainfragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.mainfragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.mainfragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.mainfragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.mainfragments.nowplaying.NowPlaying;
import com.dihanov.musiq.ui.main.mainfragments.usertopartists.UserTopArtists;
import com.dihanov.musiq.ui.main.mainfragments.usertoptracks.UserTopTracks;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int artistResultCase;
    private static int albumResultCase;
    private static int favoriteArtistCase;
    private static int favoriteAlbumCase;

    private static int nowPlayingCase;
    private static int userTopTracksCase;
    private static int userTopArtistsCase;

    private static int loggedInTabCount = 3;
    private static int loggedOutTabCount = 4;
    //default init
    private static int tabCount = loggedOutTabCount + loggedInTabCount;


    public ViewPagerAdapter(FragmentManager fm, String username) {
        super(fm);
        boolean isLoggedIn = !username.isEmpty() || !username.equals("");
        artistResultCase = isLoggedIn ? 3 : 3 - loggedInTabCount;
        albumResultCase = isLoggedIn ? 4 : 4 - loggedInTabCount;
        favoriteArtistCase = isLoggedIn ? 5 : 5 - loggedInTabCount;
        favoriteAlbumCase = isLoggedIn ? 6 : 6 - loggedInTabCount;

        nowPlayingCase = isLoggedIn ? 0 : 99;
        userTopTracksCase = isLoggedIn ? 1 : 98;
        userTopArtistsCase = isLoggedIn ? 2 : 9;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == nowPlayingCase) {
            return NowPlaying.newInstance();
        } else if (position == userTopTracksCase) {
            return UserTopTracks.newInstance();
        } else if (position == userTopArtistsCase) {
            return UserTopArtists.newInstance();
        } else if (position == artistResultCase) {
            return ArtistResult.newInstance();
        } else if (position == albumResultCase) {
            return AlbumResult.newInstance();
        } else if (position == favoriteArtistCase) {
            return FavoriteArtist.newInstance();
        } else if (position == favoriteAlbumCase) {
            return FavoriteAlbums.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabCount;
    }

    public static void setTabCount(int tabCount) {
        ViewPagerAdapter.tabCount = tabCount;
    }

    public static int getLoggedInTabCount() {
        return loggedInTabCount;
    }

    public static int getLoggedOutTabCount() {
        return loggedOutTabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == nowPlayingCase) {
            return NowPlaying.TITLE;
        } else if (position == userTopTracksCase) {
            return UserTopTracks.TITLE;
        } else if (position == userTopArtistsCase) {
            return UserTopArtists.TITLE;
        } else if (position == artistResultCase) {
            return ArtistResult.TITLE;
        } else if (position == albumResultCase) {
            return AlbumResult.TITLE;
        } else if (position == favoriteArtistCase) {
            return FavoriteArtist.TITLE;
        } else if (position == favoriteAlbumCase) {
            return FavoriteAlbums.TITLE;
        }
        return super.getPageTitle(position);
    }

    public static int getArtistResultCase() {
        return artistResultCase;
    }

    public static int getAlbumResultCase() {
        return albumResultCase;
    }

    public static int getFavoriteArtistCase() {
        return favoriteArtistCase;
    }

    public static int getFavoriteAlbumCase() {
        return favoriteAlbumCase;
    }

    public static int getNowPlayingCase() {
        return nowPlayingCase;
    }
}
