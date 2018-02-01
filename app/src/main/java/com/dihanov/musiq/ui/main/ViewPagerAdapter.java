package com.dihanov.musiq.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.main.main_fragments.album.AlbumResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResultFragment;
import com.dihanov.musiq.ui.main.main_fragments.favorites.album.FavoriteAlbumsFragment;
import com.dihanov.musiq.ui.main.main_fragments.favorites.artist.FavoriteArtistFragment;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 4;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ArtistResultFragment.newInstance();
            case 1:
                return AlbumResultFragment.newInstance();
            case 2:
                return FavoriteArtistFragment.newInstance();
            case 3:
                return FavoriteAlbumsFragment.newInstance();
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
                return ArtistResultFragment.TITLE;
            case 1:
                return AlbumResultFragment.TITLE;
            case 2:
                return FavoriteArtistFragment.TITLE;
            case 3:
                return FavoriteAlbumsFragment.TITLE;
        }
        return super.getPageTitle(position);
    }
}
