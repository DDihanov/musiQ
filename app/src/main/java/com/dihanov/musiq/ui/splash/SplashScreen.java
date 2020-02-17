package com.dihanov.musiq.ui.splash;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.ui.login.Login;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

//this class doesn't really need a presenter or dagger injections
public class SplashScreen extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private AlertDialog enableNotificationListenerAlertDialog;

    @BindView(R.id.splash_logo)
    ImageView splashLogo;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
    }

    private void handleNotificationAccess() {
        // If the user did not turn the notification listener service on we prompt him to do so
        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        } else {
            Thread timer = new Thread() {
                public void run() {
                    try {
                        HelperMethods.showTooltip(SplashScreen.this, splashLogo, SplashScreen.this.getString(R.string.welcome_string));
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleNotificationAccess();
    }

    private void checkForSavedCredentials() {
        Intent loginRedirect = new Intent(this, Login.class);
        boolean hasRememberMe = userSettingsRepository.hasRememberMeEnabled();

        if (hasRememberMe) {
            loginRedirect.putExtra(Constants.REMEMBER_ME, hasRememberMe);
        }

        loginRedirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginRedirect);
        finish();
    }

    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }
}

