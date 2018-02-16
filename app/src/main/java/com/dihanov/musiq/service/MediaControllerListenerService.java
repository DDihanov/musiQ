package com.dihanov.musiq.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.service.scrobble.Scrobbler;
import com.dihanov.musiq.ui.main.main_fragments.settings.SettingsActivity;
import com.dihanov.musiq.util.NetworkConnectionReceiver;
import com.dihanov.musiq.util.Notificator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class MediaControllerListenerService extends NotificationListenerService
        implements MediaSessionManager.OnActiveSessionsChangedListener,
        SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String CONTROLLER_PREFIX = "media_controller.";

    private static final String TAG = MediaControllerListenerService.class.getSimpleName();
    private static String currentPlayingControllerPackageName;

    @Inject
    Scrobbler scrobbler;

    private Map<String, MediaController.Callback> callbacks = new HashMap<>();
    private List<MediaController> currentControllers = new ArrayList<>();

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);

        MediaSessionManager mediaSessionManager =
                (MediaSessionManager)
                        getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        ComponentName componentName = new ComponentName(this, this.getClass());
        mediaSessionManager.addOnActiveSessionsChangedListener(this, componentName);

        //force trigger the event before user presses a button, if this is not called, then the media
        //controller will be found only after the user interacts with the media player, thus, skipping a track
        List<MediaController> initialSessions = mediaSessionManager.getActiveSessions(componentName);
        onActiveSessionsChanged(initialSessions);
        this.registerReceiver(new NetworkConnectionReceiver(scrobbler), new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
        App.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onActiveSessionsChanged(@Nullable List<MediaController> controllers) {
        boolean enableAutoDetect = App.getSharedPreferences().getBoolean("enable_auto_detect", true);

        MediaController.Callback callback = new MediaController.Callback() {
            @Override
            public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                scrobbler.setStatus(state);
            }

            @Override
            public void onMetadataChanged(@Nullable MediaMetadata metadata) {
                scrobbler.updateTrackInfo(metadata);
            }
        };


        for (ListIterator<MediaController> iterator = controllers.listIterator(); iterator.hasNext(); ) {
            MediaController element = iterator.next();
            String packageName = element.getPackageName();
            boolean contains = false;
            ListIterator<MediaController> currControllerIterator = currentControllers.listIterator();

            for (MediaController controller : currentControllers) {
                if (controller.equals(element)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                if(!enableAutoDetect){
                    if(!App.getSharedPreferences().getBoolean(SettingsActivity.PLAYER_PREFIX + element.getPackageName(), true)){
                        continue;
                    }
                }
                element.registerCallback(callback);
                currControllerIterator.add(element);
                callbacks.put(element.getPackageName(), callback);
                App.getSharedPreferences()
                        .edit()
                        .putString(CONTROLLER_PREFIX + element.getPackageName(), CONTROLLER_PREFIX + element.getPackageName())
                        .apply();
            }
        }

        for (ListIterator<MediaController> iterator = currentControllers.listIterator(); iterator.hasNext(); ) {
            MediaController element = iterator.next();
            boolean contains = false;

            for (MediaController controller : controllers) {
                if (controller.equals(element)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                element.unregisterCallback(callback);
                callbacks.remove(element.getPackageName());
                iterator.remove();
            }
        }

        //manage currently active controller info
        for (MediaController currentController : currentControllers) {
            if(currentController != null && currentController.getPlaybackState() != null){
                if(currentController.getPlaybackState().getState() == PlaybackState.STATE_PLAYING){
                    currentPlayingControllerPackageName = currentController.getPackageName();
                    break;
                }
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        if(sbn.getPackageName().equals(currentPlayingControllerPackageName)){
            Notificator.cancelNotification(this);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.startsWith(SettingsActivity.PLAYER_PREFIX)){
            String packageName = key.substring(14, key.length());

            if(!sharedPreferences.getBoolean(key, true)){
                for(ListIterator<MediaController> iterator = currentControllers.listIterator(); iterator.hasNext(); ){
                    MediaController mediaController = iterator.next();
                    if(mediaController.getPackageName().equals(packageName)){
                        if(callbacks.containsKey(packageName)){
                            mediaController.unregisterCallback(callbacks.get(packageName));
                        }
                        iterator.remove();
                    }
                }
            } else {
                onActiveSessionsChanged(currentControllers);
            }
        }
    }
}
