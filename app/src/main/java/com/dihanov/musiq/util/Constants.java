package com.dihanov.musiq.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;
import com.github.florent37.viewtooltip.ViewTooltip;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class Constants {
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String TAGS = "tags";
    public static final String LAST_SEARCH = "lastSearch";
    public static final int ARTIST_LIMIT = 6;
    public static final int ALBUM_LIMIT = 20;
    public static final int IMAGE_XLARGE = 4;
    public static final int IMAGE_LARGE = 3;
    public static final int BIRD_COLOR = Color.parseColor("#37B4E2");

    private static final String NO_NETWORK_CONN_FOUND = "ooops! i couldn't find an internet connection!";
    private static final long NETWORK_CHECK_DELAY = 10000;

    public static final String formatNumberWithSeperator(int number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }
    public static final String formatNumberWithSeperator(double number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }
    public static final String formatNumberWithSeperator(long number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }
    public static final String formatNumberWithSeperator(String number){
        return formatNumberWithSeperator(Long.parseLong(number));
    }

    public static boolean isTablet(Context context){
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static int getOrientation(Context context){
        return context.getResources().getConfiguration().orientation;
    }

    public static void setToolbarFont(CollapsingToolbarLayout toolbarLayout, Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/cabin_regular.ttf");
        toolbarLayout.setTitle(context.getString(R.string.app_name));
        toolbarLayout.setExpandedTitleTypeface(typeface);
        toolbarLayout.setCollapsedTitleTypeface(typeface);
    }

    public static void showTooltip(Activity activity, View view, String text){
        ViewTooltip.on(view)
                .corner(60)
                .textSize(TypedValue.COMPLEX_UNIT_DIP, 20)
                .align(ViewTooltip.ALIGN.CENTER)
                .textTypeFace(Typeface.createFromAsset(activity.getAssets(), "fonts/cabin_regular.ttf"))
                .position(ViewTooltip.Position.BOTTOM)
                .text(text)
                .color(Constants.BIRD_COLOR)
                .textColor(Color.WHITE)
                .animation(new ViewTooltip.FadeTooltipAnimation())
                .show();
    }
    public static void showTooltip(Activity activity, View view, String text, float textSize){
        ViewTooltip.on(view)
                .corner(60)
                .textSize(TypedValue.COMPLEX_UNIT_DIP, textSize)
                .align(ViewTooltip.ALIGN.CENTER)
                .textTypeFace(Typeface.createFromAsset(activity.getAssets(), "fonts/cabin_regular.ttf"))
                .position(ViewTooltip.Position.BOTTOM)
                .text(text)
                .color(Constants.BIRD_COLOR)
                .textColor(Color.WHITE)
                .animation(new ViewTooltip.FadeTooltipAnimation())
                .show();
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

    public static void checkConnection(MainActivity mainActivity) {
        Handler handler = new Handler();

        Thread networkCheckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     boolean isConnected = Connectivity.isConnected(mainActivity);
                    if (!isConnected) {
                        handler.post(() -> showNetworkErrorTooltip(mainActivity));
                    }
                    Thread.sleep(NETWORK_CHECK_DELAY);
                } catch (InterruptedException e) {
                    Log.e(this.getClass().toString(), e.getMessage());
                }
            }
        });

        if(!networkCheckThread.isAlive()){
            networkCheckThread.start();
        }
    }


    public static void showNetworkErrorTooltip(Activity activity) {
        Constants.showTooltip(activity, ((MainActivity)activity).getBirdIcon(), NO_NETWORK_CONN_FOUND, 15f);
    }
}
