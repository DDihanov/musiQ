package com.dihanov.musiq.util;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;

import javax.inject.Inject;

public class UserInfoSetter {

    @Inject
    public UserInfoSetter() {
    }

    public void setUserInfo(String profilePicUrl, String playcount, String username, NavigationView navigationView, Context context) {
        RelativeLayout drawerLayout = (RelativeLayout) navigationView.getHeaderView(0);

        TextView usernameTextView = (TextView) drawerLayout.getChildAt(0);
        TextView scrobbleCount = (TextView) drawerLayout.getChildAt(1);
        ImageView userAvatar = (ImageView) drawerLayout.getChildAt(2);

        usernameTextView.setVisibility(View.VISIBLE);
        scrobbleCount.setVisibility(View.VISIBLE);

        Glide.with(context)
                .load(profilePicUrl)
                .apply(RequestOptions.circleCropTransform().placeholder(context.getResources()
                        .getIdentifier("ic_missing_image", "drawable", context
                                .getPackageName()))).into(userAvatar);
        usernameTextView.setText(context.getString(R.string.logged_in_as) + " " + username);
        scrobbleCount.setText(context.getString(R.string.scrobbles) + " " + playcount);

    }

}
