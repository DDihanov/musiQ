package com.dihanov.musiq.ui.settings.profile;

import javax.inject.Inject;

public class ProfilePresenter implements ProfileContract.Presenter {
    private ProfileContract.View profileView;

    @Inject
    public ProfilePresenter() {
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
