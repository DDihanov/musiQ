package com.dihanov.musiq.ui.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.SpecificArtist;
import com.dihanov.musiq.models.Tag;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;
import com.dihanov.musiq.util.SettingsManager;
import com.dihanov.musiq.util.UserInfoSetter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.veinhorn.tagview.TagView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetails extends DaggerAppCompatActivity implements ArtistDetailsContract.View, MainViewFunctionable {
    private static final String TAG_RETAINED_ARTIST = "artist";
    private static final String TAG_RETAINED_ALBUMS = "albums";

    private String serializedArtist;
    private String serializedAlbumList;
    private Artist artist;

    @Inject
    ArtistDetailsContract.Presenter presenter;

    @Inject
    UserInfoSetter userInfoSetter;

    @Inject
    SettingsManager settingsManager;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    FavoritesManager favoritesManager;

    @BindView(R.id.navigation)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.artist_favorite)
    ImageView favoriteArtistStar;

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

    @BindView(R.id.artist_details_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.artist_details_bird)
    ImageView bird;


    private TagView firstTag, secondTag, thirdTag, fourthTag, fifthTag;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout_details);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            this.serializedArtist = savedInstanceState.getString(TAG_RETAINED_ARTIST);
            this.serializedAlbumList = savedInstanceState.getString(TAG_RETAINED_ALBUMS);
        } else {
            Intent receiveIntent = getIntent();
            String artistSerialized = receiveIntent.getStringExtra(Constants.ARTIST);
            String albumSerialized = receiveIntent.getStringExtra(Constants.ALBUM);
            this.serializedArtist = artistSerialized;
            this.serializedAlbumList = albumSerialized;
        }

        settingsManager.setActivity(this);

        initCollapsingToolbar();
        setSupportActionBar(toolbar);
        this.presenter.takeView(this);

        initNavigationDrawer();
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        initArtistSpecifics();
    }

    private void initArtistSpecifics() {
        Artist deserialized = new Gson().fromJson(serializedArtist, SpecificArtist.class).getArtist();
        Single.just(deserialized)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(__ -> showProgressBar())
                .doOnSuccess(deserializedArtist -> {
                    if (deserializedArtist == null){
                        hideProgressBar();
                        Toast.makeText(getApplicationContext(), R.string.error_could_not_find_artist, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ArtistDetails.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ArtistDetails.this.startActivity(intent);
                        return;
                    }
                    String name = (deserializedArtist.getName() == null) ? deserializedArtist.getText() : deserializedArtist.getName();
                    artist = deserializedArtist;
                    favoriteArtistStar.setClickable(true);
                    setArtistTitle(name);
                    initArtistImage();
                    hideProgressBar();
                    initViewPager();
                    initTags();
                    configureIcon();
                })
                .subscribe();
    }

    private void configureIcon() {
        if (!favoritesManager.isFavorited(Constants.FAVORITE_ARTISTS_KEY, this.artist.getName().toLowerCase())) {
            this.favoriteArtistStar.setImageResource(android.R.drawable.btn_star_big_off);
            this.favoriteArtistStar.setTag(false);
        } else {
            this.favoriteArtistStar.setImageResource(android.R.drawable.star_big_on);
            this.favoriteArtistStar.setTag(true);
        }

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

        String username = userSettingsRepository.getUsername();
        if (!username.isEmpty() && username != "") {
            drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(@NonNull View view, float v) { }

                @Override
                public void onDrawerOpened(@NonNull View view) {
                    presenter.getUserInfo();
                }

                @Override
                public void onDrawerClosed(@NonNull View view) { }

                @Override
                public void onDrawerStateChanged(int i) { }
            } );
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


    @SuppressLint("ResourceType")
    private void initTags() {
        firstTag = (TagView) findViewById(R.id.first_tag);
        secondTag = (TagView) findViewById(R.id.second_tag);
        thirdTag = (TagView) findViewById(R.id.third_tag);
        fourthTag = (TagView) findViewById(R.id.fourth_tag);
        fifthTag = (TagView) findViewById(R.id.fifth_tag);

        TagView[] tags = new TagView[]{
                firstTag, secondTag, thirdTag, fourthTag, fifthTag
        };

        List<Tag> tagText = this.artist.getTags().getTag();

        if (tagText.isEmpty()) {
            return;
        }

        //this is very ugly, however since there is no streams what can you do
        List<String> firstFive = new ArrayList<String>() {
            {
                for (int i = 0; i < tagText.size(); i++) {
                    this.add(tagText.get(i).getName());
                }
            }
        };

        //sorted by tag name length
        Collections.sort(firstFive, new Comparator<String>() {

            @Override
            public int compare(String s, String t1) {
                return Integer.compare(t1.length(), s.length());
            }
        });

        for (int i = 0; i < tagText.size(); i++) {
            TagView currTag = tags[i];
            currTag.setText(firstFive.get(i));
            currTag.setTagColor(Color.parseColor(getString(R.color.colorAccent)));
        }
    }


    private void initArtistImage() {
        Glide.with(this).load(this.artist.getImage().get(Constants.IMAGE_XLARGE).getText())
                .apply(new RequestOptions().placeholder(this.getResources()
                        .getIdentifier("ic_missing_image", "drawable", this.getPackageName()))).transition(withCrossFade(500)).into(this.artistImage);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TAG_RETAINED_ARTIST, serializedArtist);
        outState.putString(TAG_RETAINED_ALBUMS, serializedAlbumList);
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

        //need to call this as calligraphy doesnt change the fonts of the tablayout, since there is no exposed property,
        //in the xml, and the fonts are set programatically
        HelperMethods.changeTabsFont(this, tabLayout);
    }

    @OnClick(R.id.artist_favorite)
    public void onFavoriteClick() {
        if (((boolean) favoriteArtistStar.getTag()) == false) {
            this.favoriteArtistStar.setImageResource(android.R.drawable.btn_star_big_on);
            this.favoriteArtistStar.setTag(true);
            favoritesManager.addToFavorites(Constants.FAVORITE_ARTISTS_KEY,
                    this.artist.getName().toLowerCase(), new Gson().toJson(this.artist, Artist.class));
            Toast.makeText(this, "Artist added to favorites", Toast.LENGTH_SHORT).show();
        } else {
            this.favoriteArtistStar.setImageResource(android.R.drawable.btn_star_big_off);
            this.favoriteArtistStar.setTag(false);
            favoritesManager.removeFromFavorites(Constants.FAVORITE_ARTISTS_KEY,
                    this.artist.getName().toLowerCase(), new Gson().toJson(this.artist, Artist.class));
            Toast.makeText(this, "Artist removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserInfo(String profilePicUrl, String playcount, String username) {
        userInfoSetter.setUserInfo(profilePicUrl, playcount, username, navigationView, this);
    }

    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        HelperMethods.setToolbarFont(collapsingToolbar, this);
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
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                    getSupportActionBar().setHomeButtonEnabled(false);
                    isShow = true;
                } else if (isShow) {
//                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    collapsingToolbar.setTitle(" ");
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    isShow = false;
                }
            }
        });
    }

    public String getArtistBiography() {
        if (this.artist == null) {
            return " ";
        }
        return this.artist.getBio().getContent();
    }

    //this prevents the popupwindow from closing
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

    public void setArtistTitle(String artistTitle) {
        this.artistTitle.setText(artistTitle);
    }

    public String getSerialiedAlbums() {
        return this.serializedAlbumList;
    }

    public Artist getArtist() {
        return this.artist;
    }

    public ArtistDetails getDetailActivity() {
        return this;
    }
}
