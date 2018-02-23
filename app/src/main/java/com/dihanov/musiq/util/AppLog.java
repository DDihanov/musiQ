package com.dihanov.musiq.util;

import com.dihanov.musiq.BuildConfig;

/**
 * Created by dimitar.dihanov on 2/23/2018.
 */

public class AppLog {
    public static void log(String tag, String message) {
        if ((BuildConfig.DEBUG)) {
            android.util.Log.d(tag, message);
        }
    }
}

