package com.dihanov.musiq.ui.settings.profile.user_bio;

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

public class ProfileUserInfoPresenter implements ProfileUserInfoContract.Presenter {
    private final String TAG = getClass().getSimpleName();

    private ProfileUserInfoContract.View view;
    private LastFmApiClient lastFmApiClient;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Inject
    public ProfileUserInfoPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void fetchUserInfo() {
        String username = App.getSharedPreferences().getString(Constants.USERNAME, "");

        lastFmApiClient
                .getLastFmApiService()
                .getUserInfo(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        if (view != null){
                            view.showProgressBar();
                        }
                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        //let's see you crash now >:(
                        if (view != null && userInfo != null && userInfo.getUser() != null &&
                                userInfo.getUser().getRealname() != null &&
                                userInfo.getUser().getUrl() != null &&
                                userInfo.getUser().getCountry() != null &&
                                userInfo.getUser().getAge() != null &&
                                userInfo.getUser().getPlaycount() != null &&
                                userInfo.getUser().getRegistered().getUnixtime() != null) {
                            view.loadUserBio(userInfo.getUser().getRealname(),
                                    userInfo.getUser().getUrl(),
                                    userInfo.getUser().getCountry(),
                                    userInfo.getUser().getAge(),
                                    userInfo.getUser().getPlaycount(),
                                    userInfo.getUser().getRegistered().getUnixtime());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null){
                            view.hideProgressBar();
                        }
                        AppLog.log(TAG, e.getMessage());
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null){
                            view.hideProgressBar();
                        }
                        compositeDisposable.clear();
                    }
                });
    }

    @Override
    public void takeView(ProfileUserInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }
}
