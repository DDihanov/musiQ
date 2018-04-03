package com.dihanov.musiq.ui.settings.profile;

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

public class ProfilePresenter implements ProfileContract.Presenter {
    private final String TAG = getClass().getSimpleName();
    private LastFmApiClient lastFmApiClient;

    private ProfileContract.View profileView;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ProfilePresenter(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(ProfileContract.View view) {
        this.profileView = view;
    }

    @Override
    public void leaveView() {
        this.profileView = null;
    }
}
