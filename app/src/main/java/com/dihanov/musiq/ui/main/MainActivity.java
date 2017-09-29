package com.dihanov.musiq.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.KeyboardHelper;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerAppCompatActivity implements MainActivityContract.View {
    private static final String search = "search for artists";
    @Inject MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.main_gridview)
    GridView gridView;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.appbar) AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.tabs) TabLayout tabLayout;

    @BindView(R.id.viewpager) ViewPager viewPager;

    private SearchView searchBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        if (savedInstanceState == null)
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.viewpager, ArtistResultFragment.newInstance())
//                    .commitAllowingStateLoss();
        initGridView();
        initViewPager();
        initCollapsingToolbar();
        setSupportActionBar(toolbar);

        appBarLayout.setExpanded(true);
        mainActivityPresenter.takeView(this);
        mainActivityPresenter.setBackdropImageChangeListener(this);
    }



    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        Constants.changeTabsFont(this, tabLayout);
    }

    private void initCollapsingToolbar() {
        Constants.setToolbarFont(collapsingToolbar, this);
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
                    collapsingToolbar.setTitle(" ");

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.leaveView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        this.searchBar = (SearchView) myActionMenuItem.getActionView();
        return true;
    }

    @Override
    public SearchView getSearchBar() {
        return this.searchBar;
    }


    @Override
    public GridView getGridView() {
        return this.gridView;
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
    public void hideKeyboard() {
        KeyboardHelper.hideKeyboard(this);
    }

    private void initGridView() {
//        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.HORIZONTAL);
        gridView.setAdapter(new TopArtistAdapter(this, R.layout.top_artist_viewholder, new ArrayList<Artist>()));
        gridView.setSelector(android.R.color.transparent);
//        mLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
    }
    //    public void getArtistExample() {
//        Single<Artist> artistInfo = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfo("Cher");
////        Call<Artist> call = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfoCall("Cher");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        call.enqueue(new Callback<Artist>() {
////            @Override
////            public void onResponse(Call<Artist> call, Response<Artist> response) {
////                Artist artist = response.body();
////                builder.setTitle(artist.getArtist().getName());
////                builder.setMessage(artist.getArtist().getBio().getContent());
////                builder.show();
////                System.out.println();
////            }
////
////            @Override
////            public void onFailure(Call<Artist> call, Throwable t) {
////            }
////        });
//
////        artistInfo
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(artist -> {
////                            builder.setTitle(artist.getArtist().getName());
////                            builder.setMessage(artist.getArtist().getBio().getSummary());
////                            builder.show();});
////    }
}
