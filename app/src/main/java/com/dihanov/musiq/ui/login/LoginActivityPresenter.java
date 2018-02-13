package com.dihanov.musiq.ui.login;

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

public class LoginActivityPresenter implements LoginActivityContract.Presenter {
    @Inject
    LastFmApiClient lastFmApiClient;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private LoginActivity loginActivity;

    @Inject
    LoginActivityPresenter(){
    }

    @Override
    public void takeView(LoginActivityContract.View view) {
        loginActivity = (LoginActivity)view;
    }

    @Override
    public void leaveView() {
        loginActivity = null;
    }

    @Override
    public void authenticateUser(String username, String password, Context context, boolean rememberMe) {
        if (checkConnection()){
            HelperMethods.setLayoutChildrenEnabled(true, loginActivity.findViewById(R.id.login_layout));
            return;
        }

        lastFmApiClient.getLastFmApiService()
                .getMobileSessionToken(Constants.AUTH_MOBILE_SESSION_METHOD, username, password, Config.API_KEY, HelperMethods.generateSig(username, password), Config.FORMAT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        HelperMethods.setLayoutChildrenEnabled(false, loginActivity.findViewById(R.id.login_layout));
                        compositeDisposable.add(d);
                        loginActivity.showProgressBar();
                        HelperMethods.showTooltip(loginActivity, loginActivity.getBirdIcon(), loginActivity.getString((R.string.logging_in_text)));
                    }

                    @Override
                    public void onNext(User user) {
                        SharedPreferences sharedPreferences = App.getSharedPreferences();
                        sharedPreferences.edit().putString(Constants.USER_SESSION_KEY, user.getSession().getKey()).apply();
                    }

                    @Override
                    public void onError(Throwable e) {
                        HelperMethods.showTooltip(loginActivity, loginActivity.getBirdIcon(), loginActivity.getString((R.string.error_username_password)));
                        HelperMethods.setLayoutChildrenEnabled(true, loginActivity.findViewById(R.id.login_layout));
                        loginActivity.hideProgressBar();
                        Log.e(this.getClass().toString(), e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        persistUserInfo(username, password, rememberMe);
                        //if login is successful we can start the service
                        loginActivity.startService(new Intent(context.getApplicationContext(), MediaControllerListenerService.class));
                        compositeDisposable.clear();
                        loginActivity.hideProgressBar();
                        Intent intent = new Intent(loginActivity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        loginActivity.startActivity(intent);
                        loginActivity.finish();
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
        if(!Connectivity.isConnected(loginActivity)){
            HelperMethods.showNetworkErrorTooltip(loginActivity, loginActivity.getBirdIcon());
            return true;
        }
        return false;
    }
}
