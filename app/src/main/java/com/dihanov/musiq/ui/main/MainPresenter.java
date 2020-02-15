package com.dihanov.musiq.ui.main;

import android.util.Pair;

import com.dihanov.musiq.data.usecase.FetchArtistWithSerializedInfoUseCase;
import com.dihanov.musiq.data.usecase.GetChartTopArtistsUseCase;
import com.dihanov.musiq.data.usecase.GetUserInfoUseCase;
import com.dihanov.musiq.data.usecase.GetUserTopArtistsUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.models.UserTopArtists;
import com.dihanov.musiq.util.Constants;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mainActivityView;
    private GetUserInfoUseCase getUserInfoUseCase;
    private GetUserTopArtistsUseCase getUserTopArtistsUseCase;
    private GetChartTopArtistsUseCase getChartTopArtistsUseCase;
    private FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase;

    private UseCase.ResultCallback<UserInfo> userInfoResultCallback = new UseCase.ResultCallback<UserInfo>() {
        @Override
        public void onStart() {
            mainActivityView.showProgressBar();
        }

        @Override
        public void onSuccess(UserInfo userInfo) {
            if (userInfo == null || userInfo.getUser() == null ||
                    userInfo.getUser().getImage().get(Constants.IMAGE_LARGE) == null ||
                    userInfo.getUser().getPlaycount() == null) {
                return;
            }

            String profilePicUrl = userInfo.getUser().getImage().get(Constants.IMAGE_LARGE).getText();
            String playcount = userInfo.getUser().getPlaycount();

            mainActivityView.setUserInfo(profilePicUrl, playcount, userInfo.getUser().getName());
            mainActivityView.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            mainActivityView.hideProgressBar();
        }
    };

    private UseCase.ResultCallback<UserTopArtists> userTopArtistsResultCallback = new UseCase.ResultCallback<UserTopArtists>() {
        @Override
        public void onStart() {
            mainActivityView.showProgressBar();
        }

        @Override
        public void onSuccess(UserTopArtists userTopArtists) {
            if (userTopArtists == null || userTopArtists.getTopartists() == null || userTopArtists.getTopartists().getArtist() == null) {
                return;
            }
            mainActivityView.setTopArtists(userTopArtists.getTopartists().getArtist());
            mainActivityView.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            mainActivityView.hideProgressBar();
        }
    };

    private UseCase.ResultCallback<List<Artist>> listResultCallback = new UseCase.ResultCallback<List<Artist>>() {
        @Override
        public void onStart() {
            mainActivityView.showProgressBar();
        }

        @Override
        public void onSuccess(List<Artist> response) {
            mainActivityView.setTopArtists(response);
            mainActivityView.hideProgressBar();
        }

        @Override
        public void onError(Throwable e) {
            mainActivityView.hideProgressBar();
        }
    };

    private UseCase.ResultCallback<HashMap<String, String>> hashMapResultCallback = new UseCase.ResultCallback<HashMap<String, String>>() {
        @Override
        public void onStart() {
            mainActivityView.showProgressBar();
        }

        @Override
        public void onSuccess(HashMap<String, String> response) {
            mainActivityView.hideProgressBar();
            mainActivityView.startActivityWithExtras(response);
        }

        @Override
        public void onError(Throwable e) {
            mainActivityView.hideProgressBar();
        }
    };

    @Inject
    public MainPresenter(FetchArtistWithSerializedInfoUseCase fetchArtistWithSerializedInfoUseCase, GetChartTopArtistsUseCase getChartTopArtistsUseCase, GetUserInfoUseCase getUserInfoUseCase, GetUserTopArtistsUseCase getUserTopArtistsUseCase) {
        this.fetchArtistWithSerializedInfoUseCase = fetchArtistWithSerializedInfoUseCase;
        this.getChartTopArtistsUseCase = getChartTopArtistsUseCase;
        this.getUserTopArtistsUseCase = getUserTopArtistsUseCase;
        this.getUserInfoUseCase = getUserInfoUseCase;
    }

    @Override
    public void takeView(MainContract.View view) {
        this.mainActivityView = view;
    }

    @Override
    public void leaveView() {
        this.mainActivityView = null;
    }


    @Override
    public void getUserInfo() {
        getUserInfoUseCase.invoke(userInfoResultCallback, null);
    }


    @Override
    public void loadUserTopArtists(String timeframe) {
        getUserTopArtistsUseCase.invoke(userTopArtistsResultCallback, new Pair<>(timeframe, 4));
    }

    @Override
    public void loadChartTopArtists(int limit) {
        getChartTopArtistsUseCase.invoke(listResultCallback, limit);
    }

    @Override
    public void fetchArtist(String artistName) {
        fetchArtistWithSerializedInfoUseCase.invoke(hashMapResultCallback, artistName);
    }
}
