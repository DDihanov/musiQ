package com.dihanov.musiq.ui.settings.profile.userfriendsinfo;

import com.dihanov.musiq.data.usecase.BaseUseCase;
import com.dihanov.musiq.data.usecase.GetUserFriendsUseCase;
import com.dihanov.musiq.models.UserFriends;

import javax.inject.Inject;

public class ProfileUserFriendsPresenter implements ProfileUserFriendsContract.Presenter, BaseUseCase.ResultCallback<UserFriends> {
    private ProfileUserFriendsContract.View view;
    private GetUserFriendsUseCase getUserFriendsUseCase;

    @Inject
    public ProfileUserFriendsPresenter(GetUserFriendsUseCase getUserFriendsUseCase) {
        this.getUserFriendsUseCase = getUserFriendsUseCase;
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
            getUserFriendsUseCase.invoke(this, limit);
    }

    @Override
    public void onStart() {
        if (view != null){
            view.showProgressBar();
        }
    }

    @Override
    public void onSuccess(UserFriends userFriends) {
        if(userFriends != null){
            if(userFriends.getFriends() != null
                    && userFriends.getFriends().getUser() != null
                    && !userFriends.getFriends().getUser().isEmpty()
                    && view != null){
                view.loadFriends(userFriends.getFriends().getUser());
            }
        }

        if (view != null){
            view.hideProgressBar();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (view != null){
            view.hideProgressBar();
        }
    }
}
