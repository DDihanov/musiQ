package com.dihanov.musiq.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.service.MediaControllerListenerService;
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

public class Login extends DaggerAppCompatActivity implements LoginContract.View, MainViewFunctionable {
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

    @BindView(R.id.sign_up)
    TextView register;

    @Inject
    LoginContract.Presenter loginActivityPresenter;

    @Inject
    LastFmApiClient lastFmApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        register.setMovementMethod(LinkMovementMethod.getInstance());
        rememberMeCheckBox.setChecked(App.getSharedPreferences().getBoolean(Constants.REMEMBER_ME, false));
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
                        App.getSharedPreferences().edit().remove(Constants.USERNAME).remove(Constants.PASSWORD).commit();
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
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        loginActivityPresenter.leaveView();
        super.onDestroy();
    }
}
