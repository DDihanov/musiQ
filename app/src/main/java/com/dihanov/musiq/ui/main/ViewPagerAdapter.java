package com.dihanov.musiq.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlaying;
import com.dihanov.musiq.ui.main.main_fragments.user_top_artists.UserTopArtists;
import com.dihanov.musiq.ui.main.main_fragments.user_top_tracks.UserTopTracks;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 6;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return NowPlaying.newInstance();
            case 1:
                return UserTopTracks.newInstance();
            case 2:
                return UserTopArtists.newInstance();
            case 3:
                return ArtistResult.newInstance();
            case 4:
                return AlbumResult.newInstance();
            case 5:
                return FavoriteArtist.newInstance();
            case 6:
                return FavoriteAlbums.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return NowPlaying.TITLE;
            case 1:
                return UserTopTracks.TITLE;
            case 2:
                return UserTopArtists.TITLE;
            case 3:
                return ArtistResult.TITLE;
            case 4:
                return AlbumResult.TITLE;
            case 5:
                return FavoriteArtist.TITLE;
            case 6:
                return FavoriteAlbums.TITLE;
        }
        return super.getPageTitle(position);
    }
}
