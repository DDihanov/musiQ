package com.dihanov.musiq.ui.settings.profile.user_friends_info;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.UserFriends;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProfileUserFriendsPresenter implements ProfileUserFriendsContract.Presenter {
    private final String TAG = getClass().getSimpleName();

    private LastFmApiClient lastFmApiClient;
    private ProfileUserFriendsContract.View view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ProfileUserFriendsPresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ProfileUserFriendsContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }

    @Override
    public void fetchFriends(int limit) {
        lastFmApiClient.getLastFmApiService()
                .getUserFriends(App.getSharedPreferences().getString(Constants.USERNAME, ""), 1, limit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UserFriends>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        view.showProgressBar();
                    }

                    @Override
                    public void onNext(UserFriends userFriends) {
                        if(userFriends != null){
                            if(userFriends.getFriends() != null
                                    && userFriends.getFriends().getUser() != null
                                    && !userFriends.getFriends().getUser().isEmpty()){
                                view.loadFriends(userFriends.getFriends().getUser());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.log(TAG, e.getMessage());
                        view.hideProgressBar();
                        compositeDisposable.clear();
                    }

                    @Override
                    public void onComplete() {
                        view.hideProgressBar();
                        compositeDisposable.clear();
                    }
                });
    }
}
