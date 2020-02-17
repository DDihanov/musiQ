package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.data.usecase.GetUserInfoUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.UserInfo;
import com.dihanov.musiq.util.Constants;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public class ArtistDetailsPresenter implements
        ArtistDetailsContract.Presenter,
        UseCase.ResultCallback<UserInfo> {

    private UserSettingsRepository userSettingsRepository;
    private ArtistDetailsContract.View artistDetailsActivity;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    public ArtistDetailsPresenter(UserSettingsRepository userSettingsRepository, GetUserInfoUseCase getUserInfoUseCase) {
        this.userSettingsRepository = userSettingsRepository;
        this.getUserInfoUseCase = getUserInfoUseCase;
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
        getUserInfoUseCase.invoke(this, null);
    }

    @Override
    public void onStart() {
        artistDetailsActivity.showProgressBar();
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

        artistDetailsActivity.setUserInfo(profilePicUrl, playcount, userInfo.getUser().getName());
        artistDetailsActivity.hideProgressBar();
    }

    @Override
    public void onError(Throwable e) {
        artistDetailsActivity.hideProgressBar();
    }
}
