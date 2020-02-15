package com.dihanov.musiq.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainActivity;
import com.github.florent37.viewtooltip.ViewTooltip;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by dimitar.dihanov on 2/9/2018.
 */

public class HelperMethods {
    public static String formatNumberWithSeperator(int number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }

    public static Typeface createTypefaceFromFont(Context context, String font){
        return Typeface.createFromAsset(context.getAssets(), font);
    }


    public static float getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density  = activity.getResources().getDisplayMetrics().density;

        return displayMetrics.widthPixels / density;
    }

    public static float getScreenHeight(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float density  = activity.getResources().getDisplayMetrics().density;

        return displayMetrics.heightPixels / density;
    }

    public static String formatNumberWithSeperator(double number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }

    public static String formatNumberWithSeperator(long number){
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator(' ');
        formatter.setDecimalFormatSymbols(symbols);

        return formatter.format(number);
    }

    public static String formatNumberWithSeperator(String number){
        return formatNumberWithSeperator(Long.parseLong(number));
    }

    //dp to pixel
    public static int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static boolean isTablet(BaseView<?> context){
        return ((Activity)context).getResources().getBoolean(R.bool.isTablet);
    }

    public static int getOrientation(BaseView<?> context){
        return ((Activity)context).getResources().getConfiguration().orientation;
    }

    public static int getOrientation(Activity context){
        return context.getResources().getConfiguration().orientation;
    }

    public static void setToolbarFont(CollapsingToolbarLayout toolbarLayout, Context context){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/cabin_regular.ttf");
        toolbarLayout.setExpandedTitleTypeface(typeface);
        toolbarLayout.setCollapsedTitleTypeface(typeface);
    }

    public static void showTooltip(Activity activity, View view, String text){
        ViewTooltip.on(view)
                .corner(60)
                .color(R.color.colorSecondary)
                .textSize(TypedValue.COMPLEX_UNIT_DIP, 20)
                .align(ViewTooltip.ALIGN.CENTER)
                .textTypeFace(Typeface.createFromAsset(activity.getAssets(), "fonts/cabin_regular.ttf"))
                .position(ViewTooltip.Position.BOTTOM)
                .text(text)
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
                .color(R.color.colorSecondary)
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

    public static void checkConnection(Context context) {
        Handler handler = new Handler();

        Thread networkCheckThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                     boolean isConnected = Connectivity.isConnected(context);
                    if (!isConnected) {
                        handler.post(() -> showNetworkErrorTooltip(context));
                    }
                    Thread.sleep(Constants.NETWORK_CHECK_DELAY);
                } catch (InterruptedException e) {
                    Log.e(this.getClass().toString(), e.getMessage());
                }
            }
        });

        if(!networkCheckThread.isAlive()){
            networkCheckThread.start();
        }
    }

    public static void showNetworkErrorTooltip(Context activity) {
        showTooltip((Activity)activity, ((MainActivity)activity).getBirdIcon(), Constants.NO_NETWORK_CONN_FOUND, 15f);
    }

    public static void showNetworkErrorTooltip(BaseView<?> activity, View view) {
        showTooltip((Activity)activity, view, Constants.NO_NETWORK_CONN_FOUND, 15f);
    }

    public static void setLayoutChildrenEnabled(boolean status, ViewGroup viewGroup){
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setEnabled(status);
        }
    }

    public static void setLayoutChildrenVisibility(int visibility, ViewGroup viewGroup){
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setVisibility(visibility);
        }
    }

    public static byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static int determineArtistLimit(BaseView mainActivity) {
        if (HelperMethods.isTablet(mainActivity) || HelperMethods.getOrientation(mainActivity) == Configuration.ORIENTATION_LANDSCAPE) {
            return 10;
        }

        return 6;
    }
}
