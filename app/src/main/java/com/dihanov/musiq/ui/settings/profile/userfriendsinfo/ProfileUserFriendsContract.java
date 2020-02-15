package com.dihanov.musiq.ui.settings.profile.userfriendsinfo;

import com.dihanov.musiq.models.User;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import java.util.List;

public interface ProfileUserFriendsContract {
    interface View extends BaseView<Presenter> {
        void loadFriends(List<User> friends);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View> {
        void fetchFriends(int limit);
    }
}
