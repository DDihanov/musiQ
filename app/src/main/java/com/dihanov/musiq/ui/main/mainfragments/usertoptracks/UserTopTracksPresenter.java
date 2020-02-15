package com.dihanov.musiq.ui.main.mainfragments.usertoptracks;

import com.dihanov.musiq.data.usecase.BaseUseCase;
import com.dihanov.musiq.data.usecase.GetUserTopTracksUseCase;
import com.dihanov.musiq.models.UserTopTracks;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopTracksPresenter implements UserTopTracksContract.Presenter, BaseUseCase.ResultCallback<UserTopTracks> {
    private UserTopTracksContract.View userTopTracksFragment;
    private GetUserTopTracksUseCase getUserTopTracksUseCase;

    @Inject
    public UserTopTracksPresenter(GetUserTopTracksUseCase getUserTopTracksUseCase) {
        this.getUserTopTracksUseCase = getUserTopTracksUseCase;
    }

    @Override
    public void takeView(UserTopTracksContract.View view) {
        this.userTopTracksFragment = view;
    }


    @Override
    public void leaveView() {
        this.userTopTracksFragment = null;
    }

    @Override
    public void loadTopTracks(String timeframe) {
        getUserTopTracksUseCase.invoke(this, timeframe);
    }

    @Override
    public void onStart() {
        userTopTracksFragment.showProgressBar();
    }

    @Override
    public void onSuccess(UserTopTracks response) {
        if (userTopTracksFragment == null) return;
        userTopTracksFragment.configureBarChart(response);
        userTopTracksFragment.hideProgressBar();
    }

    @Override
    public void onError(Throwable e) {
        userTopTracksFragment.hideProgressBar();
    }
}
