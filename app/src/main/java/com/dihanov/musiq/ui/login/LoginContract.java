package com.dihanov.musiq.ui.login;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void redirectToMain();

        void toggleChildrenAvailability(boolean enabled);

        void showInvalidLogin();

        void showLoginSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void authenticateUser(String username, String password, boolean rememberMe);
    }
}
