package com.dihanov.musiq.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.TopArtistAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.util.ActivityStarterWithIntentExtras;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.dihanov.musiq.util.KeyboardHelper;
import com.dihanov.musiq.util.SettingsManager;
import com.dihanov.musiq.util.TopArtistSource;
import com.dihanov.musiq.util.UserInfoSetter;

import org.codechimp.apprater.AppRater;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import tourguide.tourguide.Overlay;
import tourguide.tourguide.ToolTip;
import tourguide.tourguide.TourGuide;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerAppCompatActivity implements MainContract.View, AbstractAdapter.OnItemClickedListener<Artist> {
    private static final String TAG_LAST_SEARCH = "lastSearch";

    @Inject
    MainContract.Presenter mainActivityPresenter;

    @Inject
    ActivityStarterWithIntentExtras activityStarterWithIntentExtras;

    @Inject
    UserInfoSetter userInfoSetter;

    @Inject
    SettingsManager settingsManager;

    @BindView(R.id.dummy_button)
    Button dummyButton;

    @BindView(R.id.bird)
    ImageView bird;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.navigation)
    NavigationView navigationView;

    @BindView(R.id.main_gridview)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private SearchView searchBar;
    private String lastSearch;
    private NetworkChangeReceiver networkChangeReceiver;

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (isOnline(context)) {
                    loadBackdrop((MainActivity) context);
                } else {
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

        private boolean isOnline(Context context) {
            return Connectivity.isConnected(context);
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_main);
        ButterKnife.bind(this);
        AppRater.setPackageName("com.dihanov.musiq");
        AppRater.app_launched(this, 1, 2);
        AppRater.setVersionCodeCheckEnabled(true);


        if (savedInstanceState != null) {
            this.lastSearch = savedInstanceState.getString(TAG_LAST_SEARCH);
        }

        settingsManager.setActivity(this);

        initGridView();
        initViewPager();
        initCollapsingToolbar();
        setSupportActionBar(toolbar);

        initNavigationDrawer();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        appBarLayout.setExpanded(true);
        mainActivityPresenter.takeView(this);

        if (App.getSharedPreferences().getBoolean(Constants.FIRST_TIME, true)) {
            int orientation = this.getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                setUpTutorial();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpTutorial() {
        dummyButton.setVisibility(View.VISIBLE);
        Overlay overlay = new Overlay()
                .setBackgroundColor(R.color.overlay)
                .disableClick(true)
                .setStyle(Overlay.Style.CIRCLE);

        TourGuide mTourGuideHandler = TourGuide.init(this)
                .setToolTip(new ToolTip().setTitle(getString(R.string.welcome)).setDescription(getString(R.string.tutorial_text_guide)))
                .setOverlay(overlay)
                .playOn(dummyButton);

        dummyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTourGuideHandler.cleanUp();
                dummyButton.setVisibility(View.INVISIBLE);
                App.getSharedPreferences().edit().putBoolean(Constants.FIRST_TIME, false).apply();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
                return true;
            }
        });
    }


    private void initNavigationDrawer() {
        navigationView.setNavigationItemSelectedListener(item -> {
            // set item as selected to persist highlight
            item.setChecked(true);
            // close drawer when item is tapped
            drawerLayout.closeDrawers();

            settingsManager.manageSettings(item);
            return true;
        });
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");
        if (!username.isEmpty() && username != "") {
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float v) {
                }

                @Override
                public void onDrawerOpened(@NonNull View view) {
                    mainActivityPresenter.getUserInfo();
                }

                @Override
                public void onDrawerClosed(@NonNull View view) {
                }

                @Override
                public void onDrawerStateChanged(int i) {
                }
            });
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String s = "";
        if (searchBar != null) {
            s = searchBar.getQuery().toString();
        }
        outState.putString(Constants.LAST_SEARCH, s);
    }

    private void initViewPager() {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");
        boolean isLoggedIn = !username.isEmpty() || !username.equals("");


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (!isLoggedIn) {
            viewPagerAdapter.setTabCount(viewPagerAdapter.getLoggedOutTabCount());
        } else {
            viewPagerAdapter.setTabCount(viewPagerAdapter.getLoggedInTabCount() + viewPagerAdapter.getLoggedOutTabCount());
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //need to call this as calligraphy doesnt change the fonts of the tablayout, since there is no exposes property,
        //in the xml, and the fonts are set programatically
        HelperMethods.changeTabsFont(this, tabLayout);
    }

    private void initCollapsingToolbar() {
        HelperMethods.setToolbarFont(collapsingToolbar, this);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    searchBar.setVisibility(View.INVISIBLE);
                    collapsingToolbar.setTitle(" ");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                    isShow = true;
                } else if (isShow) {
                    searchBar.setVisibility(View.VISIBLE);
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPager();
        this.invalidateOptionsMenu();
    }

    @Override
    protected void onStart() {
        super.onStart();

        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(networkChangeReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (networkChangeReceiver != null) {
            this.unregisterReceiver(networkChangeReceiver);
        }
        mainActivityPresenter.leaveView();
    }

    private void loadBackdrop(MainActivity mainActivity) {
        int limit = HelperMethods.determineArtistLimit(mainActivity);

        int which = App.getSharedPreferences().getInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS);

        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        if (username.isEmpty() || username.equals("")) {
            mainActivityPresenter.loadChartTopArtists(limit);
            return;
        }

        if (which == TopArtistSource.LAST_FM_CHARTS) {
            mainActivityPresenter.loadChartTopArtists(limit);
        } else {
            String period = HelperMethods.determineSelectedTimeframeFromInt();
            mainActivityPresenter.loadUserTopArtists(period, username, limit);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        this.searchBar = (SearchView) myActionMenuItem.getActionView();

        if (this.lastSearch != null) {
            this.searchBar.setQuery(lastSearch, true);
        }

        return true;
    }

    public SearchView getSearchBar() {
        return this.searchBar;
    }

    //prevent popupwindow close on rotation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public View getBirdIcon() {
        return this.bird;
    }

    @Override
    public void hideKeyboard() {
        KeyboardHelper.hideKeyboard(this);
    }

    @Override
    public void setUserInfo(String profilePicUrl, String playcount, String username) {
        userInfoSetter.setUserInfo(profilePicUrl, playcount, username, navigationView, this);
    }


    @Override
    public void setTopArtists(List<Artist> artists) {
        TopArtistAdapter topArtistAdapter = new TopArtistAdapter(this,
                artists,
                true,
                this);
        recyclerView.setAdapter(topArtistAdapter);
        recyclerView.postDelayed(() -> recyclerView.smoothScrollToPosition(artists.size() - 1), 1000);
    }

    private void initGridView() {
        RecyclerView.LayoutManager layoutManager = new CustomGridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    public void setViewPagerSelection(int position) {
        this.viewPager.setCurrentItem(position, true);
    }

    @Override
    public void onItemClicked(Artist item) {
        mainActivityPresenter.fetchArtist(item.getName());
    }

    @Override
    public void startActivityWithExtras(HashMap<String, String> bundleExtra) {
        bundleExtra.put(Constants.LAST_SEARCH, searchBar.getQuery().toString());
        activityStarterWithIntentExtras.startActivityWithExtras(bundleExtra, this, ArtistDetails.class);
    }
}
