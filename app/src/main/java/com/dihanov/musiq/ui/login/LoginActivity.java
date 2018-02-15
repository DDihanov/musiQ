package com.dihanov.musiq.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public class LoginActivity extends DaggerAppCompatActivity implements LoginActivityContract.View, MainViewFunctionable {
    @BindView(R.id.sign_in)
    Button signInButton;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.continue_without_sign_in)
    TextView continueWithoutLogin;

    @BindView(R.id.login_bird)
    TextView logo;

    @BindView(R.id.login_progress)
    ProgressBar progressBar;

    @BindView(R.id.remember_me_checkbox)
    AppCompatCheckBox rememberMeCheckBox;

    @Inject
    LoginActivityContract.Presenter loginActivityPresenter;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        rememberMeCheckBox.setChecked(App.getSharedPreferences().getBoolean(Constants.REMEMBER_ME, false));
        loginActivityPresenter.takeView(LoginActivity.this);
    }

    @Override
    protected void onResume() {
        checkIntent(getIntent());
        super.onResume();
    }

    @OnClick(R.id.sign_in)
    public void onSignInClick(View view){
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        loginActivityPresenter.authenticateUser(usernameString, passwordString, this, rememberMeCheckBox.isChecked());
    }

    private void checkIntent(Intent intent) {
        SharedPreferences sharedPreferences = App.getSharedPreferences();
        if(intent.hasExtra(Constants.REMEMBER_ME)){
            String username = sharedPreferences.getString(Constants.USERNAME, "");
            String password = sharedPreferences.getString(Constants.PASSWORD, "");
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.login_layout);
            HelperMethods.setLayoutChildrenEnabled(false, layout);
            this.username.setText(username);
            this.password.setText(password);
            loginActivityPresenter.authenticateUser(username, password, this, rememberMeCheckBox.isChecked());
        }
    }

    @OnClick(R.id.continue_without_sign_in)
    public void onClick(TextView editText){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public View getBirdIcon() {
        return this.logo;
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        loginActivityPresenter.leaveView();
        super.onDestroy();
    }
}
