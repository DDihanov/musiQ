package com.dihanov.musiq.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsActivity extends DaggerAppCompatActivity implements ArtistDetailsActivityContract.View {
    @Inject
    ArtistDetailsActivityPresenter presenter;

    @BindView(R.id.artist_details_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.artist_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.artist_details_image)
    ImageView artistImage;

    @BindView(R.id.artist_details_tabs)
    TabLayout tabLayout;

    @BindView(R.id.artist_details_viewpager)
    ViewPager viewPager;

    @BindView(R.id.artist_details_name)
    TextView artistTitle;

    private Artist artist;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

//        if (savedInstanceState == null)
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.artist_details_collapsing_toolbar, ArtistResultFragment.newInstance())
//                    .commitAllowingStateLoss();

        Intent receiveIntent = getIntent();
        String artistSerialized = receiveIntent.getStringExtra(Constants.ARTIST);
        this.artist = new Gson().fromJson(artistSerialized, Artist.class);

        initCollapsingToolbar();
        setSupportActionBar(toolbar);
        this.presenter.takeView(this);

        initViewPager();
        initArtistImage();
        setArtistTitle(artist.getName());
    }

    private void initArtistImage() {
        Glide.with(this).load(this.artist.getImage().get(Constants.IMAGE_XLARGE).getText()).crossFade(2000).into(this.artistImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter.leaveView();
    }

    private void initViewPager() {
        ArtistDetailsViewPagerAdapter viewPagerAdapter = new ArtistDetailsViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //need to call this as calligraphy doesnt change the fonts of the tablayout, since there is no exposes property,
        //in the xml, and the fonts are set programatically
        Constants.changeTabsFont(this, tabLayout);
    }

    private void initCollapsingToolbar() {
        Constants.setToolbarFont(collapsingToolbar, this);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.artist_details_appbar);
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
    public String getArtistBiography() {
        return this.artist.getBio().getContent();
    }

    @Override
    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setArtistTitle(String artistTitle) {
        this.artistTitle.setText(artistTitle);
    }
}
