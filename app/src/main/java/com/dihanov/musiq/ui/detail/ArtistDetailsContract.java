package com.dihanov.musiq.ui.detail;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public interface ArtistDetailsContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void setUserInfo(String profilePicUrl, String playcount, String username);
    }

    interface Presenter extends BasePresenter<View> {
        void getUserInfo();
    }
}
