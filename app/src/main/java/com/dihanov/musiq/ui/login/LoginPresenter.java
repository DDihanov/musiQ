package com.dihanov.musiq.ui.login;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.db.UserSettingsRepository;
import com.dihanov.musiq.models.User;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.util.AppLog;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.SigGenerator;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public class LoginPresenter implements LoginContract.Presenter {
    private LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LoginContract.View login;
    private UserSettingsRepository userSettingsRepository;
    private SigGenerator sigGenerator;

    @Inject
    public LoginPresenter(LastFmApiClient lastFmApiClient, UserSettingsRepository userSettingsRepository, SigGenerator sigGenerator){
        this.lastFmApiClient = lastFmApiClient;
        this.userSettingsRepository = userSettingsRepository;
        this.sigGenerator = sigGenerator;
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
        lastFmApiClient.getLastFmApiService()
                .getMobileSessionToken(Constants.AUTH_MOBILE_SESSION_METHOD, username, password, Config.API_KEY, sigGenerator.generateAuthSig(username, password), Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                        login.showProgressBar();
                        login.toggleChildrenAvailability(false);
                        login.showLoginSuccess();
                    }

                    @Override
                    public void onNext(User user) {
                        userSettingsRepository.persistSessionKey(user.getSession().getKey());
                    }

                    @Override
                    public void onError(Throwable e) {
                        login.showInvalidLogin();
                        login.toggleChildrenAvailability(true);
                        login.hideProgressBar();
                        AppLog.log(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        persistUserInfo(username, password, rememberMe);
                        compositeDisposable.clear();
                        login.redirectToMain();

                    }
                });
    }


    private void persistUserInfo(String username, String password, boolean rememberMe) {
        userSettingsRepository.persistUsername(username);
        userSettingsRepository.persistPassword(password);
        userSettingsRepository.setRememberMe(rememberMe);
    }

    public void setLastFmApiClient(LastFmApiClient lastFmApiClient) {
        this.lastFmApiClient = lastFmApiClient;
    }
}
