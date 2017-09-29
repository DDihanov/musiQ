package com.dihanov.musiq.ui.detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.detail.detail_fragments.ArtistDetailsBiographyFragment;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 1;

    public ArtistDetailsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ArtistDetailsBiographyFragment.newInstance();
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
                return ArtistDetailsBiographyFragment.TITLE;
//            case 1:
//                return ArtistResultFragment.TITLE;
//            case 2:
        }
        return super.getPageTitle(position);
    }
}
