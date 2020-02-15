package com.dihanov.musiq.ui.settings.profile;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.settings.profile.userbio.ProfileUserInfo;
import com.dihanov.musiq.ui.settings.profile.userfriendsinfo.ProfileUserFriendsInfo;
import com.dihanov.musiq.ui.settings.profile.userlovedtracks.UserLovedTracksView;

public class ProfileViewPagerAdapter extends FragmentStatePagerAdapter {
    private static int TAB_COUNT = 3;

    public ProfileViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return ProfileUserInfo.newInstance();
            case 1:
                return ProfileUserFriendsInfo.newInstance();
            case 2:
                return UserLovedTracksView.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return ProfileUserInfo.TITLE;
            case 1:
                return ProfileUserFriendsInfo.TITLE;
            case 2:
                return UserLovedTracksView.TITLE;
        }
        return null;
    }
}
