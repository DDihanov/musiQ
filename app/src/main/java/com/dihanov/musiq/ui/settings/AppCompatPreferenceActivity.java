package com.dihanov.musiq.ui.settings;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.TopArtistSource;

/**
 * A {@link android.preference.PreferenceActivity} which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
public abstract class AppCompatPreferenceActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getDelegate().installViewFactory();
        getDelegate().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        getDelegate().onPostCreate(savedInstanceState);
    }

    public ActionBar getSupportActionBar() {
        return getDelegate().getSupportActionBar();
    }

    public void setSupportActionBar(@Nullable Toolbar toolbar) {
        getDelegate().setSupportActionBar(toolbar);
    }

    @Override
    public MenuInflater getMenuInflater() {
        return getDelegate().getMenuInflater();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        getDelegate().setContentView(layoutResID);
    }

    @Override
    public void setContentView(View view) {
        getDelegate().setContentView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().setContentView(view, params);
    }

    @Override
    public void addContentView(View view, ViewGroup.LayoutParams params) {
        getDelegate().addContentView(view, params);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        getDelegate().onPostResume();
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        getDelegate().setTitle(title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getDelegate().onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getDelegate().onDestroy();
    }

    public void invalidateOptionsMenu() {
        getDelegate().invalidateOptionsMenu();
    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }

    @Override
    public void onHeaderClick(Header header, int position) {
        super.onHeaderClick(header, position);
        if (header.id == R.id.top_artist_source_header || header.id == R.id.scrobble_review_header) {
            showNoAccountDialog();
        } else {
            createListPreferenceDialog();
        }
    }

    private void showNoAccountDialog() {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");
        if (username.isEmpty() || username.equals("")) {
            AlertDialog.Builder b = new AlertDialog.Builder(AppCompatPreferenceActivity.this);
            b.setTitle(getString(R.string.note));
            b.setMessage(getString(R.string.log_in_to_use_feature));
            b.setCancelable(true);
            b.setNeutralButton(getString(R.string.dialog_action_dismiss), (dialog, which) -> dialog.dismiss());
            b.create().show();
        }
    }

    private void createListPreferenceDialog() {
        Dialog dialog;
        final String[] str = getResources().getStringArray(R.array.top_artist_source);
        int selectedItem = App.getSharedPreferences().getInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS);
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(this.getString(R.string.top_artist_chart_source));
        b.setSingleChoiceItems(str, selectedItem, (dialog1, which) -> {
            if (which == 0) {
                App.getSharedPreferences().edit().putInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS).apply();
            }
            if (which == 1) {
                App.getSharedPreferences().edit().putInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.USER_TOP_ARTISTS_CHART).apply();
                int selectedTimeframe = App.getSharedPreferences().getInt(Constants.USER_TOP_ARTIST_CHART_TIMEFRAME, 0);
                b.setTitle(getString(R.string.select_timeframe));
                b.setSingleChoiceItems(R.array.timeframe_options, selectedTimeframe, (dialog2, which1) -> {
                    App.getSharedPreferences().edit().putInt(Constants.USER_TOP_ARTIST_CHART_TIMEFRAME, which1).apply();
                    dialog2.dismiss();
                });
                b.create().show();
            }
        });

        dialog = b.create();
        dialog.show();
    }
}
