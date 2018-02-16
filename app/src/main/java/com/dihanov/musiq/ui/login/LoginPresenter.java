package com.dihanov.musiq.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.dihanov.musiq.R;
import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.User;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

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
    private final LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LoginContract.View loginActivity;

    @Inject
    LoginPresenter(LastFmApiClient lastFmApiClient){
        this.lastFmApiClient = lastFmApiClient;
    }

    @Override
    public void takeView(LoginContract.View view) {
        loginActivity = view;
    }

    @Override
    public void leaveView() {
        loginActivity = null;
    }

    @Override
    public void authenticateUser(String username, String password, Context context, boolean rememberMe) {
        Login login = ((Login)this.loginActivity);
        if (checkConnection()){
            HelperMethods.setLayoutChildrenEnabled(true, login.findViewById(R.id.login_layout));
            return;
        }

        lastFmApiClient.getLastFmApiService()
                .getMobileSessionToken(Constants.AUTH_MOBILE_SESSION_METHOD, username, password, Config.API_KEY, HelperMethods.generateSig(username, password), Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        HelperMethods.setLayoutChildrenEnabled(false, login.findViewById(R.id.login_layout));
                        compositeDisposable.add(d);
                        login.showProgressBar();
                        HelperMethods.showTooltip(login, login.getBirdIcon(), login.getString((R.string.logging_in_text)));
                    }

                    @Override
                    public void onNext(User user) {
                        SharedPreferences sharedPreferences = App.getSharedPreferences();
                        sharedPreferences.edit().putString(Constants.USER_SESSION_KEY, user.getSession().getKey()).apply();
                    }

                    @Override
                    public void onError(Throwable e) {
                        HelperMethods.showTooltip(login, login.getBirdIcon(), login.getString((R.string.error_username_password)));
                        HelperMethods.setLayoutChildrenEnabled(true, login.findViewById(R.id.login_layout));
                        login.hideProgressBar();
                        Log.e(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        persistUserInfo(username, password, rememberMe);
                        //if login is successful we can start the service
                        login.startService(new Intent(context.getApplicationContext(), MediaControllerListenerService.class));
                        compositeDisposable.clear();
                        login.hideProgressBar();
                        Intent intent = new Intent(login, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        login.startActivity(intent);
                        login.finish();
                    }
                });

    }

    private void persistUserInfo(String username, String password, boolean rememberMe) {
        SharedPreferences sharedPreferences = App.getSharedPreferences();
        sharedPreferences.edit().putString(Constants.USERNAME, username)
                .putString(Constants.PASSWORD, password)
                .putBoolean(Constants.REMEMBER_ME, rememberMe)
                .apply();
    }

    private boolean checkConnection() {
        if(!Connectivity.isConnected((Context)loginActivity)){
            HelperMethods.showNetworkErrorTooltip((Activity)loginActivity, loginActivity.getBirdIcon());
            return true;
        }
        return false;
    }
}
