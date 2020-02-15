package com.dihanov.musiq.ui.settings.profile.userbio;

import com.dihanov.musiq.data.usecase.GetUserInfoUseCase;
import com.dihanov.musiq.data.usecase.UseCase;
import com.dihanov.musiq.models.UserInfo;

import javax.inject.Inject;

public class ProfileUserInfoPresenter implements ProfileUserInfoContract.Presenter, UseCase.ResultCallback<UserInfo> {
    private ProfileUserInfoContract.View view;
    private GetUserInfoUseCase getUserInfoUseCase;

    @Inject
    public ProfileUserInfoPresenter(GetUserInfoUseCase getUserInfoUseCase) {
        this.getUserInfoUseCase = getUserInfoUseCase;
    }

    @Override
    public void fetchUserInfo() {
        getUserInfoUseCase.invoke(this, null);
    }

    @Override
    public void takeView(ProfileUserInfoContract.View view) {
        this.view = view;
    }

    @Override
    public void leaveView() {
        this.view = null;
    }

    @Override
    public void onStart() {
        if (view != null){
            view.showProgressBar();
        }
    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        if (view != null){
            view.hideProgressBar();
        }
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
    }
}
