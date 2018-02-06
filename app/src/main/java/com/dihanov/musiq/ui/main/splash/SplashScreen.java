package com.dihanov.musiq.ui.main.splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
//    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
//    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private AlertDialog enableNotificationListenerAlertDialog;

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
//        handleNotificationAccess();
    }

//    private void handleNotificationAccess() {
//        // If the user did not turn the notification listener service on we prompt him to do so
//        if(!isNotificationServiceEnabled()){
//            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
//            enableNotificationListenerAlertDialog.show();
//        } else {
//            Thread timer = new Thread() {
//                public void run() {
//                    try {
//                        Constants.showTooltip(SplashScreen.this, splashLogo, SplashScreen.this.getString(R.string.welcome_string));
//                        //Display for 3 seconds
//                        sleep(3000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    } finally {
//                        checkForSavedCredentials();
//                    }
//                }
//            };
//            timer.start();
//        }
//    }

    @Override
    protected void onResume() {
//        handleNotificationAccess();
        super.onResume();
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

//    private boolean isNotificationServiceEnabled(){
//        String pkgName = getPackageName();
//        final String flat = Settings.Secure.getString(getContentResolver(),
//                ENABLED_NOTIFICATION_LISTENERS);
//        if (!TextUtils.isEmpty(flat)) {
//            final String[] names = flat.split(":");
//            for (int i = 0; i < names.length; i++) {
//                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
//                if (cn != null) {
//                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }
//
//    private AlertDialog buildNotificationServiceAlertDialog(){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setTitle(R.string.notification_listener_service);
//        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
//        alertDialogBuilder.setPositiveButton(R.string.yes,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
//                    }
//                });
//        alertDialogBuilder.setNegativeButton(R.string.no,
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // If you choose to not enable the notification listener
//                        // the app. will not work as expected
//                    }
//                });
//        return(alertDialogBuilder.create());
//    }
}

