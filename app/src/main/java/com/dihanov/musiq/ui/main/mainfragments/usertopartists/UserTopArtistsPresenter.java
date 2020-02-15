package com.dihanov.musiq.ui.main.mainfragments.usertopartists;

import android.util.Pair;

import com.dihanov.musiq.data.usecase.GetUserTopArtistsUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.UserTopArtists;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class UserTopArtistsPresenter implements UserTopArtistsContract.Presenter,
        UseCase.ResultCallback<UserTopArtists> {
    private UserTopArtistsContract.View userTopTracks;
    private GetUserTopArtistsUseCase getUserTopArtistsUseCase;

    @Inject
    public UserTopArtistsPresenter(GetUserTopArtistsUseCase getUserTopArtistsUseCase) {
        this.getUserTopArtistsUseCase = getUserTopArtistsUseCase;
    }

    @Override
    public void takeView(UserTopArtistsContract.View view) {
        this.userTopTracks = view;
    }


    @Override
    public void leaveView() {
        this.userTopTracks = null;
    }

    @Override
    public void loadTopArtists(String timeframe) {
        getUserTopArtistsUseCase.invoke(this, new Pair<>(timeframe, 10));
    }

    @Override
    public void onStart() {
        userTopTracks.showProgressBar();
    }

    @Override
    public void onSuccess(UserTopArtists response) {
        if (userTopTracks != null) {
            userTopTracks.configureBarChart(response);
            userTopTracks.hideProgressBar();
        }
    }

    @Override
    public void onError(Throwable e) {
        userTopTracks.hideProgressBar();
    }
}
