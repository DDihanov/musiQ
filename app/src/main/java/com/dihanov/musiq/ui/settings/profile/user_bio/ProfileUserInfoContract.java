package com.dihanov.musiq.ui.settings.profile.user_bio;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

public interface ProfileUserInfoContract {
    interface View extends BaseView<Presenter> {
        void loadUserBio(String realName, String profileUrl, String country, String age, String playcount, String unixRegistrationDate);

        void showProgressBar();

        void hideProgressBar();
    }

    interface Presenter extends BasePresenter<View> {
        void fetchUserInfo();
    }
}
