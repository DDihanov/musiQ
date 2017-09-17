package com.dihanov.musiq.config;

import com.dihanov.musiq.BuildConfig;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class Config {
     public static final String API_KEY = BuildConfig.API_KEY;
     public static final String LAST_FM_API_URL = "http://ws.audioscrobbler.com/2.0/";
     public static final String LAST_FM_CALL = API_KEY + "&format=json";
     public static final String FORMAT = "json";
}
