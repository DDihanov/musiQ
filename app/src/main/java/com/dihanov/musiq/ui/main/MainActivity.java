package com.dihanov.musiq.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentPresenter;
import com.dihanov.musiq.util.KeyboardHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerAppCompatActivity implements MainActivityContract.View {
    @Inject MainActivityPresenter mainActivityPresenter;

    @Inject ArtistResultFragmentPresenter artistResultFragmentPresenter;

    @BindView(R.id.search) EditText searchEditText;

    @BindView(R.id.progress_bar) ProgressBar progressBar;

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.appbar) AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.tabs) TabLayout tabLayout;

    @BindView(R.id.viewpager) ViewPager viewPager;

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

        initCollapsingToolbar();
        initViewPager();
        setSupportActionBar(toolbar);

        //TODO: FIX THIS
        artistResultFragmentPresenter.addOnTextViewTextChangedObserver(searchEditText);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener());
        appBarLayout.setExpanded(true);
        mainActivityPresenter.takeView(this);
    }



    private void initViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
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
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
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

    private class OnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        boolean isShow = false;
        int scrollRange = -1;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar.setTitle(getString(R.string.app_name));
                isShow = true;
            } else if (isShow) {
                collapsingToolbar.setTitle(" ");
                isShow = false;
            }
        }
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
