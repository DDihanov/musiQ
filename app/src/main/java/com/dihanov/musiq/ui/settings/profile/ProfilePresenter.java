package com.dihanov.musiq.ui.settings.profile;

import com.dihanov.musiq.service.LastFmApiClient;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

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
