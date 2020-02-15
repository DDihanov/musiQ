package com.dihanov.musiq.util;

import android.util.Log;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.db.UserSettingsRepository;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SigGenerator {
    private UserSettingsRepository userSettingsRepository;

    @Inject
    public SigGenerator(UserSettingsRepository userSettingsRepository) {
        this.userSettingsRepository = userSettingsRepository;
    }

    public String generateSig(String... params){
        TreeMap<String, String> sorted = new TreeMap<>();
        String toCypher = "";
        String md5 = "";
        sorted.put("api_key", Config.API_KEY);
        sorted.put("sk", userSettingsRepository.getUserSessionKey());
        for (int i = 0; i < params.length - 1; i+=2) {
            sorted.put(params[i], params[i+1]);
        }

        for (Map.Entry<String, String> stringStringEntry : sorted.entrySet()) {
            toCypher += stringStringEntry.getKey() + stringStringEntry.getValue();
        }
        toCypher += Config.API_SECRET;
        try {
            byte[] arr = MessageDigest.getInstance("MD5").digest(toCypher.getBytes("UTF-8"));
            md5 = byteArrayToHex(arr);
        } catch (NoSuchAlgorithmException e) {
            Log.e(Constants.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(Constants.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }

        return md5;
    }


    public String generateAuthSig(String username, String password){
        String toCypher = "api_key" + Config.API_KEY + "methodauth.getMobileSessionpassword" + password + "username" + username + Config.API_SECRET;
        String md5 = "";
        try {
            byte[] arr = MessageDigest.getInstance("MD5").digest(toCypher.getBytes("UTF-8"));
            md5 = byteArrayToHex(arr);
        } catch (NoSuchAlgorithmException e) {
            Log.e(Constants.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            Log.e(Constants.class.getSimpleName(), e.getMessage());
            e.printStackTrace();
        }

        return md5;
    }


    private static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

}
