package com.dihanov.musiq.ui.main.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.ui.login.LoginActivity;
import com.dihanov.musiq.util.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

//this class doesn't really need a presenter or dagger injections
public class SplashScreen extends AppCompatActivity {
    @BindView(R.id.splash_logo)
    TextView splashLogo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Thread timer = new Thread() {
            public void run() {
                try {
                    Constants.showTooltip(SplashScreen.this, splashLogo, SplashScreen.this.getString(R.string.welcome_string));
                    //Display for 3 seconds
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    checkForSavedCredentials();
                }
            }
        };
        timer.start();
    }

    private void checkForSavedCredentials() {
        Intent loginRedirect = new Intent(this, LoginActivity.class);
        SharedPreferences sharedPreferences = App.getSharedPreferences();

        if (sharedPreferences.contains(Constants.USERNAME) && sharedPreferences.contains(Constants.PASSWORD)) {
            loginRedirect.putExtra(Constants.USERNAME, sharedPreferences.getString(Constants.USERNAME, ""));
            loginRedirect.putExtra(Constants.PASSWORD, sharedPreferences.getString(Constants.PASSWORD, ""));
        }

        loginRedirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginRedirect);
    }
}
