package com.dihanov.musiq.ui.main;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.ArtistDetailsIntentShowableImpl;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.adapters.TopArtistAdapter;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.dihanov.musiq.util.TopArtistSource;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainPresenter extends ArtistDetailsIntentShowableImpl implements MainContract.Presenter {
    private static final long NETWORK_CHECK_THREAD_TIMEOUT = 5000;
    private static int TOP_ARTIST_LIMIT = 6;
    private final String TAG = this.getClass().getSimpleName();

    private MainContract.View mainActivityView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final LastFmApiClient lastFmApiClient;

    @Inject
    public MainPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(MainContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        compositeDisposable.clear();
        this.mainActivityView = null;
    }


    @Override
    public void setBackdropImageChangeListener(MainContract.View mainActivity) {
        setListenerOnFoundConnection(mainActivity);
    }

    @Override
    public void setOnDrawerOpenedListener(MainContract.View mainActivity) {
        DrawerLayout drawerLayout = mainActivity.getDrawerLayout();
        NavigationView navigationView = mainActivity.getNavigationView();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

                lastFmApiClient.getLastFmApiService()
                        .getUserInfo(username)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Observer<UserInfo>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                compositeDisposable.add(d);
                                mainActivity.showProgressBar();
                            }

                            @Override
                            public void onNext(UserInfo userInfo) {
                                if (userInfo == null || userInfo.getUser() == null ||
                                        userInfo.getUser().getImage().get(Constants.IMAGE_LARGE) == null ||
                                        userInfo.getUser().getPlaycount() == null) {
                                    return;
                                }

                                String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
                                String playcount = userInfo.getUser().getPlaycount();

                                setProfileInfo(profilePicUrl, playcount, navigationView, mainActivity, username);
                            }

                            @Override
                            public void onError(Throwable e) {
                                mainActivity.hideProgressBar();
                                AppLog.log(TAG, e.getMessage());
                                compositeDisposable.clear();
                            }

                            @Override
                            public void onComplete() {
                                compositeDisposable.clear();
                                mainActivity.hideProgressBar();
                            }
                        });

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void setProfileInfo(String profilePicUrl, String playcount, NavigationView navigationView, MainContract.View mainActivity, String username) {
        RelativeLayout drawerLayout = (RelativeLayout) navigationView.getHeaderView(0);

        TextView usernameTextView = (TextView) drawerLayout.getChildAt(0);
        TextView scrobbleCount = (TextView) drawerLayout.getChildAt(1);
        ImageView userAvatar = (ImageView) drawerLayout.getChildAt(2);

        usernameTextView.setVisibility(View.VISIBLE);
        scrobbleCount.setVisibility(View.VISIBLE);

        Glide.with(mainActivity.getContext())
                .load(profilePicUrl)
                .apply(RequestOptions.circleCropTransform().placeholder(mainActivity.getContext().getResources()
                        .getIdentifier("ic_account_circle_black_24dp", "drawable", mainActivity.getContext()
                                .getPackageName())))
                .transition(withCrossFade(2000)).into(userAvatar);
        usernameTextView.setText(mainActivity.getContext().getString(R.string.logged_in_as) + " " + username);
        scrobbleCount.setText(mainActivity.getContext().getString(R.string.scrobbles) + " " + playcount);

        App.getSharedPreferences().edit().putString(Constants.PROFILE_PIC, profilePicUrl).apply();
    }


    private void loadBackdrop(MainContract.View mainActivity) {
        TOP_ARTIST_LIMIT = HelperMethods.determineArtistLimit(mainActivity);

        int which = App.getSharedPreferences().getInt(Constants.TOP_ARTIST_SOURCE, TopArtistSource.LAST_FM_CHARTS);

        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        if (username.isEmpty() || username.equals("")) {
            loadChartTopArtists(mainActivity);
            return;
        }

        if (which == TopArtistSource.LAST_FM_CHARTS) {
            loadChartTopArtists(mainActivity);
        } else {
            String period = HelperMethods.determineSelectedTimeframeFromInt();
            loadUserTopArtists(mainActivity, period, username);
        }
    }

    public void loadUserTopArtists(MainContract.View mainActivity, String timeframe, String username) {
        lastFmApiClient.getLastFmApiService()
                .getUserTopArtists(username, TOP_ARTIST_LIMIT, timeframe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserTopArtists>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        HelperMethods.showTooltip(mainActivity, mainActivity.getBirdIcon(), mainActivity.getContext().getString(R.string.top_artists_text));
                        mainActivity.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(UserTopArtists userTopArtists) {
                        if (userTopArtists == null || userTopArtists.getTopartists() == null || userTopArtists.getTopartists().getArtist() == null) {
                            return;
                        }
                        List<Artist> artists = userTopArtists.getTopartists().getArtist();
                        TopArtistAdapter topArtistAdapter = new TopArtistAdapter(mainActivity,
                                (ArrayList<Artist>) artists,
                                MainPresenter.this,
                                true);
                        mainActivity.getRecyclerView().setAdapter(topArtistAdapter);
                        if (!artists.isEmpty()) {
                            mainActivity.getRecyclerView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mainActivity.getRecyclerView().smoothScrollToPosition(artists.size() - 1);
                                }
                            }, 1000);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mainActivity.hideProgressBar();
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }

    private void loadChartTopArtists(MainContract.View mainActivity) {
        lastFmApiClient.getLastFmApiService().chartTopArtists(TOP_ARTIST_LIMIT)
                .map(topArtistsResult -> topArtistsResult.getArtists().getArtistMatches())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Artist>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        HelperMethods.showTooltip(mainActivity, mainActivity.getBirdIcon(), mainActivity.getContext().getString(R.string.top_artists_text));
                        mainActivity.showProgressBar();
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Artist> artists) {
                        TopArtistAdapter topArtistAdapter = new TopArtistAdapter(mainActivity,
                                (ArrayList<Artist>) artists,
                                MainPresenter.this,
                                true);
                        mainActivity.getRecyclerView().setAdapter(topArtistAdapter);
                        mainActivity.getRecyclerView().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mainActivity.getRecyclerView().smoothScrollToPosition(artists.size() - 1);
                            }
                        }, 1000);
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                        mainActivity.hideProgressBar();
                    }

                    @Override
                    public void onComplete() {
                        mainActivity.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }

    private void setListenerOnFoundConnection(MainContract.View mainActivity) {
        Thread newConnThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!Connectivity.isConnected(mainActivity.getContext())) {
                    try {
                        HelperMethods.showNetworkErrorTooltip(mainActivity.getContext());
                        Thread.sleep(NETWORK_CHECK_THREAD_TIMEOUT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadBackdrop(mainActivity);
                    }
                });
            }
        });

        if (!newConnThread.isAlive()) {
            newConnThread.start();
        }
    }


    @Override
    public void addOnArtistResultClickedListener(ClickableArtistViewHolder viewHolder, String artistName) {
        RxView.clicks(viewHolder.getThumbnail())
                .debounce(400, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    this.showArtistDetailsIntent(artistName, mainActivityView);
                });
    }
}
