package com.dihanov.musiq.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.login.Login;
import com.dihanov.musiq.ui.settings.Settings;
import com.dihanov.musiq.ui.settings.profile.Profile;

import javax.inject.Inject;

/**
 * Created by dimitar.dihanov on 2/15/2018.
 */

public class SettingsManager {
    private Activity activity;
    private UserSettingsRepository userSettingsRepository;

    @Inject
    public SettingsManager(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private void openAbout() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        View layout = activity.getLayoutInflater().inflate(R.layout.about_layout, null, false);
        TextView aboutMessage = layout.findViewById(R.id.about_message);
        SpannableString s =
                new SpannableString(String.format(activity.getString(R.string.about_message), Constants.APP_VESRION));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        aboutMessage.setMovementMethod(LinkMovementMethod.getInstance());
        aboutMessage.setText(s);
        alertDialogBuilder.setTitle(R.string.about);
        if (!activity.isFinishing()) {
            final AlertDialog alertDialog = alertDialogBuilder
                    .setView(layout)
                    .setCancelable(true)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setPositiveButton(R.string.dialog_action_dismiss, null)
                    .create();
            alertDialog.show();
        }
    }

    private void openSettings() {
        activity.startActivity(new Intent(activity, Settings.class));
    }

    private void logOut() {
        userSettingsRepository.clearLoginData();
        Intent intent = new Intent(activity, Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.stopService(new Intent(activity, MediaControllerListenerService.class));
        activity.startActivity(intent);
        activity.finish();
    }

    public void manageSettings(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.nav_profile):
                openProfile();
                break;
            case (R.id.nav_settings):
                openSettings();
                break;
            case (R.id.nav_logout):
                logOut();
                break;
            case (R.id.nav_about):
                openAbout();
                break;
            default:
                break;
        }
    }

    private void openProfile() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        String username = userSettingsRepository.getUsername();

        if (username.isEmpty() || username == "" && !activity.isFinishing()) {
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setTitle(activity.getString(R.string.note))
                    .setMessage(activity.getString(R.string.log_in_to_use_feature))
                    .setNeutralButton(activity.getString(R.string.dialog_action_dismiss), (dialog, which) -> {
                        dialog.dismiss();
                    });
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }

        activity.startActivity(new Intent(activity, Profile.class));
    }
}
