package com.dihanov.musiq.util;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ActivityStarterWithIntentExtras {
    @Inject
    public ActivityStarterWithIntentExtras() {
    }

    public void startActivityWithExtras(HashMap<String, String> bundleExtra, Activity startingActivity, Class<? extends Activity> otherActivity) {
        Intent showArtistDetailsIntent = new Intent(startingActivity, otherActivity);
        for (Map.Entry<String, String> kvp : bundleExtra.entrySet()) {
            showArtistDetailsIntent.putExtra(kvp.getKey(), kvp.getValue());
        }
        startingActivity.startActivity(showArtistDetailsIntent);
    }
}
