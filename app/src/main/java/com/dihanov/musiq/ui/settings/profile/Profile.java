package com.dihanov.musiq.ui.settings.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class Profile extends DaggerAppCompatActivity implements ProfileContract.View {
    @Inject
    ProfileContract.Presenter profilePresenter;

    @BindView(R.id.profile_collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;

    @BindView(R.id.profile_image)
    ImageView profileImage;

    @BindView(R.id.profile_tabs)
    TabLayout tabLayout;

    @BindView(R.id.profile_viewpager)
    ViewPager viewPager;

    @BindView(R.id.profile_name)
    TextView profileName;

    @BindView(R.id.profile_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.profile_bird)
    TextView bird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        initCollapsingToolbar();
        setSupportActionBar(toolbar);
        this.profilePresenter.takeView(this);
        String profilePicUrl = App.getSharedPreferences().getString(Constants.PROFILE_PIC, "");
        if (!profilePicUrl.isEmpty() && profilePicUrl != "") {
            setUserImage(profilePicUrl);
        }

        initViewPager();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void initViewPager() {
        ProfileViewPagerAdapter viewPagerAdapter = new ProfileViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //need to call this as calligraphy doesnt change the fonts of the tablayout, since there is no exposed property,
        //in the xml, and the fonts are set programatically
        HelperMethods.changeTabsFont(this, tabLayout);
    }

    @Override
    public void setUserImage(String url) {
        Glide.with(this).load(url).apply(RequestOptions.circleCropTransform()).transition(withCrossFade(2000)).into(profileImage);
    }

    private void initCollapsingToolbar() {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");
        collapsingToolbar.setTitle(username);
        HelperMethods.setToolbarFont(collapsingToolbar, this);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.profile_appbar);
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
                    collapsingToolbar.setTitle(username);
                    isShow = false;
                }
            }
        });
    }

    @Override
    public View getBirdIcon() {
        return this.bird;
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.profilePresenter.leaveView();
    }
}
