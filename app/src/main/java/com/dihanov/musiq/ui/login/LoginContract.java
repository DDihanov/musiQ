package com.dihanov.musiq.ui.login;

import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public interface LoginContract {
    interface View extends BaseView<Presenter> {
        void showProgressBar();

        android.view.View getBirdIcon();

        void redirectToMain(CompositeDisposable compositeDisposable);
    }

    interface Presenter extends BasePresenter<View> {
        void authenticateUser(String username, String password, LoginContract.View context, boolean rememberMe);
    }
}
