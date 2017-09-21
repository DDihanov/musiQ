package com.dihanov.musiq.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragment;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 1;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ArtistResultFragment.newInstance();
//            case 1:
//                return ArtistResultFragment.newInstance();
//            case 2:
//               break;
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
//            case 1:
//                return ArtistResultFragment.TITLE;
//            case 2:
        }
        return super.getPageTitle(position);
    }
}
