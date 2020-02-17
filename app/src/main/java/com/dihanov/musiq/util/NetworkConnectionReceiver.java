package com.dihanov.musiq.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dihanov.musiq.data.repository.scrobble.ScrobbleRepository;

/**
 * Created by dimitar.dihanov on 2/9/2018.
 */

public class NetworkConnectionReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkConnectionReceiver.class.getSimpleName();
    private ScrobbleRepository scrobbleRepository;

    public NetworkConnectionReceiver(ScrobbleRepository scrobbleRepository) {
        this.scrobbleRepository = scrobbleRepository;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.getState() == NetworkInfo.State.CONNECTED) {
                scrobbleRepository.scrobbleFromCache();
                AppLog.log(TAG, "Scrobbling cached tracks.");
            }
        }
    }
}
