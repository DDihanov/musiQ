package com.dihanov.musiq.ui.login;

import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.data.usecase.AuthenticateUserUseCase;
import com.dihanov.musiq.data.usecase.BaseUseCase;
import com.dihanov.musiq.models.AuthenticateUserModel;
import com.dihanov.musiq.models.User;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public class LoginPresenter implements LoginContract.Presenter, BaseUseCase.ResultCallback<User> {
    private LoginContract.View login;
    private UserSettingsRepository userSettingsRepository;
    private AuthenticateUserUseCase authenticateUserUseCase;
    private AuthenticateUserModel authenticateUserModel;

    @Inject
    public LoginPresenter(UserSettingsRepository userSettingsRepository, AuthenticateUserUseCase authenticateUserUseCase){
        this.userSettingsRepository = userSettingsRepository;
        this.authenticateUserUseCase = authenticateUserUseCase;
    }

    @Override
    public void takeView(LoginContract.View view) {
        login = view;
    }

    @Override
    public void leaveView() {
        login = null;
    }

    @Override
    public void authenticateUser(String username, String password, boolean rememberMe) {
        AuthenticateUserModel authenticateUserModel = new AuthenticateUserModel(username, password, rememberMe);
        this.authenticateUserModel = authenticateUserModel;
        authenticateUserUseCase.invoke(this, authenticateUserModel);
    }


    private void persistUserInfo(String username, String password, boolean rememberMe) {
        userSettingsRepository.persistUsername(username);
        userSettingsRepository.persistPassword(password);
        userSettingsRepository.setRememberMe(rememberMe);
    }

    @Override
    public void onStart() {
        login.showProgressBar();
        login.toggleChildrenAvailability(false);
        login.showLoginSuccess();
    }

    @Override
    public void onSuccess(User response) {
        userSettingsRepository.persistSessionKey(response.getSession().getKey());
        persistUserInfo(authenticateUserModel.getUsername(), authenticateUserModel.getPassword(), authenticateUserModel.isShouldRemember());
        login.redirectToMain();
    }

    @Override
    public void onError(Throwable e) {
        login.showInvalidLogin();
        login.toggleChildrenAvailability(true);
        login.hideProgressBar();
    }
}
