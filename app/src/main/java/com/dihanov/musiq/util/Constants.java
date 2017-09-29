package com.dihanov.musiq.util;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class Constants {
    public static final String ARTIST = "artist";
    public static final int ARTIST_LIMIT = 6;
    public static final int IMAGE_XLARGE = 4;
    public static final int IMAGE_LARGE = 3;

    public static void setToolbarFont(CollapsingToolbarLayout toolbarLayout, Context context){
        toolbarLayout.setTitle(context.getString(R.string.app_name));
        toolbarLayout.setExpandedTitleTypeface(ResourcesCompat.getFont(context, R.font.cabin));
        toolbarLayout.setCollapsedTitleTypeface(ResourcesCompat.getFont(context, R.font.cabin));
    }

    public static void changeTabsFont(Context context, TabLayout tabLayout) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    TextView toEdit = (TextView) tabViewChild;
//                    toEdit.setTypeface(ResourcesCompat.getFont(context, R.font.cabin));
                    toEdit.setAllCaps(false);
                }
            }
        }
    }
}
