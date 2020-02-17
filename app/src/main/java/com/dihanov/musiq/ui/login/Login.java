package com.dihanov.musiq.ui.login;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.network.LastFmApiClient;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Connectivity;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by dimitar.dihanov on 2/5/2018.
 */

public class Login extends DaggerAppCompatActivity implements LoginContract.View, MainViewFunctionable {
    @BindView(R.id.sign_in)
    MaterialButton signInButton;

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.continue_without_sign_in)
    TextView continueWithoutLogin;

    @BindView(R.id.login_logo)
    ImageView logo;

    @BindView(R.id.login_progress)
    ProgressBar progressBar;

    @BindView(R.id.remember_me_checkbox)
    AppCompatCheckBox rememberMeCheckBox;

    @BindView(R.id.sign_up)
    TextView register;

    @BindView(R.id.login_layout)
    ConstraintLayout loginLayout;

    @Inject
    LoginContract.Presenter loginActivityPresenter;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        register.setMovementMethod(LinkMovementMethod.getInstance());
        rememberMeCheckBox.setChecked(userSettingsRepository.hasRememberMeEnabled());
        loginActivityPresenter.takeView(Login.this);
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

        if (checkConnection()){
            HelperMethods.setLayoutChildrenEnabled(true, loginLayout);
        } else {
            loginActivityPresenter.authenticateUser(usernameString, passwordString, rememberMeCheckBox.isChecked());
        }
    }

    private boolean checkConnection() {
        if(!Connectivity.isConnected(this)){
            HelperMethods.showNetworkErrorTooltip(this, getBirdIcon());
            return true;
        }
        return false;
    }

    private void checkIntent(Intent intent) {
        if(intent.hasExtra(Constants.REMEMBER_ME)){
            String username = userSettingsRepository.getUsername();
            String password = userSettingsRepository.getPassword();
            ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.login_layout);
            HelperMethods.setLayoutChildrenEnabled(false, layout);
            this.username.setText(username);
            this.password.setText(password);
            loginActivityPresenter.authenticateUser(username, password, rememberMeCheckBox.isChecked());
        }
    }

    @OnClick(R.id.continue_without_sign_in)
    public void onClick(TextView textView){
        Activity loginActivity = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle(R.string.note).setMessage(R.string.without_login_warning);
        alertDialogBuilder
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(true)
                .setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userSettingsRepository.clearLoginData();
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        loginActivity.finish();
                    }
                })
                .setNegativeButton(R.string.canc, null)
                .create()
                .show();
    }

    @Override
    public View getBirdIcon() {
        return this.logo;
    }

    @Override
    public void redirectToMain() {
        //if login is successful we can start the service
        this.startService(new Intent(this.getApplicationContext(), MediaControllerListenerService.class));
        this.hideProgressBar();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void toggleChildrenAvailability(boolean enabled) {
        HelperMethods.setLayoutChildrenEnabled(enabled, loginLayout);
    }

    @Override
    public void showInvalidLogin() {
        HelperMethods.showTooltip(this, getBirdIcon(), getString((R.string.error_username_password)));
    }

    @Override
    public void showLoginSuccess() {
        HelperMethods.showTooltip(this, getBirdIcon(),  getString((R.string.logging_in_text)));
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
