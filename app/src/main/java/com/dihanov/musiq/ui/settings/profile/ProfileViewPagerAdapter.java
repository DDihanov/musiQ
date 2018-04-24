package com.dihanov.musiq.ui.settings.profile;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dihanov.musiq.ui.settings.profile.user_bio.ProfileUserInfo;
import com.dihanov.musiq.ui.settings.profile.user_friends_info.ProfileUserFriendsInfo;
import com.dihanov.musiq.ui.settings.profile.user_loved_tracks.UserLovedTracksView;

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
