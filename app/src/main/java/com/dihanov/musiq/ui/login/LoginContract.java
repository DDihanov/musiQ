package com.dihanov.musiq.ui.login;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        void showProgressBar();

        android.view.View getBirdIcon();

        void redirectToMain();

        <T extends android.view.View> T findViewById(int id);

        String getString(int id);
    }

    interface Presenter extends BasePresenter<View> {
        void authenticateUser(String username, String password, LoginContract.View context, boolean rememberMe);
    }
}
