package com.dihanov.musiq.ui.detail;

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
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsPresenter implements ArtistDetailsContract.Presenter {
    private final String TAG = this.getClass().getSimpleName();

    @Inject
    LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArtistDetailsContract.View artistDetailsActivity;

    @Inject
    public ArtistDetailsPresenter(){
    }

    @Override
    public void takeView(ArtistDetailsContract.View view) {
        this.artistDetailsActivity = view;
    }

    @Override
    public void leaveView() {
        this.artistDetailsActivity = null;
    }

    @Override
    public void setOnDrawerOpenedListener(ArtistDetailsContract.View detailsActivity) {
        DrawerLayout drawerLayout = detailsActivity.getDrawerLayout();
        NavigationView navigationView = detailsActivity.getNavigationView();

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
                                detailsActivity.showProgressBar();
                            }

                            @Override
                            public void onNext(UserInfo userInfo) {
                                String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
                                String playcount = userInfo.getUser().getPlaycount();

                                RelativeLayout drawerLayout = (RelativeLayout)navigationView.getHeaderView(0);

                                TextView usernameTextView = (TextView)drawerLayout.getChildAt(0);
                                TextView scrobbleCount = (TextView) drawerLayout.getChildAt(1);
                                ImageView userAvatar = (ImageView) drawerLayout.getChildAt(2);

                                usernameTextView.setVisibility(View.VISIBLE);
                                scrobbleCount.setVisibility(View.VISIBLE);

                                Glide.with(detailsActivity.getContext())
                                        .load(profilePicUrl)
                                        .apply(RequestOptions.circleCropTransform()).into(userAvatar);
                                usernameTextView.setText(detailsActivity.getContext().getString(R.string.logged_in_as) + " " + username);
                                scrobbleCount.setText(detailsActivity.getContext().getString(R.string.scrobbles) + " " + playcount);
                            }

                            @Override
                            public void onError(Throwable e) {
                                detailsActivity.hideProgressBar();
                                AppLog.log(TAG, e.getMessage());
                                compositeDisposable.clear();
                            }

                            @Override
                            public void onComplete() {
                                compositeDisposable.clear();
                                detailsActivity.hideProgressBar();
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
}
