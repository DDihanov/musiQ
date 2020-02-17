package com.dihanov.musiq.ui.settings;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * A {@link android.preference.PreferenceActivity} which implements and proxies the necessary calls
 * to be used with AppCompat.
 */
public abstract class AppCompatPreferenceActivity extends PreferenceActivity {
    private AppCompatDelegate mDelegate;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
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
        if (header.id == R.id.top_artist_source_header) {
            if (!isNotLoggedIn()) {
                createListPreferenceDialog();
            }
        } else if (header.id == R.id.scrobble_review_header) {
            if (isNotLoggedIn()) {
                return;
            }
        }
        super.onHeaderClick(header, position);
    }

    private boolean isNotLoggedIn() {
        String username = userSettingsRepository.getUsername();
        boolean result = username.isEmpty() || username.equals("");
        if (result) {
            AlertDialog.Builder b = new AlertDialog.Builder(AppCompatPreferenceActivity.this);
            b.setTitle(getString(R.string.note));
            b.setMessage(getString(R.string.log_in_to_use_feature));
            b.setCancelable(true);
            b.setNeutralButton(getString(R.string.dialog_action_dismiss), (dialog, which) -> dialog.dismiss());
            b.create().show();
        }

        return result;
    }

    private void createListPreferenceDialog() {
        Dialog dialog = null;
        final String[] str = getResources().getStringArray(R.array.top_artist_source);
        int selectedItem = userSettingsRepository.getSelectedChartToShow();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle(this.getString(R.string.top_artist_chart_source));
        b.setSingleChoiceItems(str, selectedItem, (dialog1, which) -> {
            if (which == 0) {
                userSettingsRepository.setChartToLastFmChart();
            }
            if (which == 1) {
                userSettingsRepository.setChartToUserTopArtists();
                int selectedTimeframe = userSettingsRepository.getChartTimeFrame();
                b.setTitle(getString(R.string.select_timeframe));
                b.setSingleChoiceItems(R.array.timeframe_options, selectedTimeframe, (dialog2, which1) -> {
                    userSettingsRepository.persistSelectedChartTimeframe(selectedItem);
                    dialog1.dismiss();
                    dialog2.dismiss();
                });
                b.create().show();
            }
        });

        dialog = b.create();
        dialog.show();
    }
}
