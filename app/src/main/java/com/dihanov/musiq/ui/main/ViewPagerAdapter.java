package com.dihanov.musiq.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResult;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResult;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbums;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtist;
import com.dihanov.musiq.ui.main.main_fragments.now_playing.NowPlaying;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 5;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return NowPlaying.newInstance();
            case 1:
                return ArtistResult.newInstance();

            case 2:
                return AlbumResult.newInstance();

            case 3:
                return FavoriteArtist.newInstance();

            case 4:
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
                return ArtistResult.TITLE;
            case 2:
                return AlbumResult.TITLE;
            case 3:
                return FavoriteArtist.TITLE;
            case 4:
                return FavoriteAlbums.TITLE;
        }
        return super.getPageTitle(position);
    }
}
