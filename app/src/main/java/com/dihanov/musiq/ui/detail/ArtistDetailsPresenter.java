package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.db.UserSettingsRepository;
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

    private UserSettingsRepository userSettingsRepository;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ArtistDetailsContract.View artistDetailsActivity;

    @Inject
    public ArtistDetailsPresenter(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
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
    public void getUserInfo() {
        String username = userSettingsRepository.getUsername();
        lastFmApiClient.getLastFmApiService()
                .getUserInfo(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        artistDetailsActivity.showProgressBar();
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        if ( userInfo == null || userInfo.getUser() == null ||
                                userInfo.getUser().getImage().get(Constants.IMAGE_LARGE) == null ||
                                userInfo.getUser().getPlaycount() == null) {
                            return;
                        }

                        String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
                        String playcount = userInfo.getUser().getPlaycount();

                        setUserInfo(profilePicUrl, playcount, username);
                    }

                    @Override
                    public void onError(Throwable e) {
                        artistDetailsActivity.hideProgressBar();
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        compositeDisposable.clear();
                        artistDetailsActivity.hideProgressBar();
                    }
                });
    }

    private void setUserInfo(String profilePicUrl, String playcount, String username) {
        userSettingsRepository.persistProfilePictureUrl(profilePicUrl);

        artistDetailsActivity.setUserInfo(profilePicUrl, playcount, username);
    }
}
