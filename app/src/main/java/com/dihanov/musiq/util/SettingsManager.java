package com.dihanov.musiq.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.service.MediaControllerListenerService;
import com.dihanov.musiq.ui.login.LoginActivity;
import com.dihanov.musiq.ui.main.main_fragments.settings.SettingsActivity;

/**
 * Created by dimitar.dihanov on 2/15/2018.
 */

public class SettingsManager {
    private Activity activity;

    public SettingsManager(Activity activity) {
        this.activity = activity;
    }

    private void openAbout() {
        View layout  = activity.getLayoutInflater().inflate(R.layout.about_layout, null);
        TextView aboutMessage = layout.findViewById(R.id.about_message);
        SpannableString s =
                new SpannableString(activity.getString(R.string.about_message));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        aboutMessage.setMovementMethod(LinkMovementMethod.getInstance());
        aboutMessage.setText(s);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                activity);
        alertDialogBuilder.setTitle(R.string.about);
        alertDialogBuilder
                .setView(layout)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton(R.string.dialog_action_dismiss, null)
                .create()
                .show();
    }

    private void openSettings() {
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }

    private void logOut() {
        App.getSharedPreferences().edit().remove(Constants.USERNAME)
                .remove(Constants.PASSWORD)
                .remove(Constants.USER_SESSION_KEY)
                .remove(Constants.REMEMBER_ME)
                .apply();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.stopService(new Intent(activity, MediaControllerListenerService.class));
        activity.startActivity(intent);
        activity.finish();
    }

    public void manageSettings(long id){
        switch ((int) id) {
            case (1):
                openSettings();
                break;
            case (2):
                logOut();
                break;
            case (3):
                openAbout();
                break;
            default:
                break;
        }
    }
}
