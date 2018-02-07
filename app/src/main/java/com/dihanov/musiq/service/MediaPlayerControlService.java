package com.dihanov.musiq.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dihanov.musiq.di.app.App;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by dimitar.dihanov on 2/6/2018.
 */

public class MediaPlayerControlService extends NotificationListenerService
        implements MediaSessionManager.OnActiveSessionsChangedListener{
    private static final String TAG = MediaPlayerControlService.class.getSimpleName();

    @Inject
    Scrobbler scrobbler;

    private SharedPreferences sharedPreferences = App.getSharedPreferences();
    private List<MediaController> currentControllers = new ArrayList<>();

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);

        MediaSessionManager mediaSessionManager =
                (MediaSessionManager)
                        getApplicationContext().getSystemService(Context.MEDIA_SESSION_SERVICE);

        ComponentName componentName = new ComponentName(this, this.getClass());
        mediaSessionManager.addOnActiveSessionsChangedListener(this, componentName);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onActiveSessionsChanged(@Nullable List<MediaController> controllers) {
        List<MediaController> toAdd = new ArrayList<>();

        MediaController.Callback callback = new MediaController.Callback() {
            @Override
            public void onPlaybackStateChanged(@NonNull PlaybackState state) {
                super.onPlaybackStateChanged(state);
            }

            @Override
            public void onMetadataChanged(@Nullable MediaMetadata metadata) {
                super.onMetadataChanged(metadata);
            }
        };

        for (ListIterator<MediaController> iterator = currentControllers.listIterator(); iterator.hasNext(); ) {
            MediaController element = iterator.next();
            boolean contains = false;

            for (MediaController controller : controllers) {
                if(controller.getPackageName().equals(element.getPackageName())){
                    contains = true;
                    break;
                }
            }

            if(!contains){
                element.unregisterCallback(callback);
                iterator.remove();
            }
        }

        for (MediaController controller : controllers) {
            boolean contains = false;
            for (MediaController currentController : currentControllers) {
                if(currentController.getPackageName().equals(controller.getPackageName())){
                    contains = true;
                    break;
                }
            }

            if(!contains){
                controller.registerCallback(callback);
                toAdd.add(controller);
            }
        }

        currentControllers.addAll(toAdd);
    }


}
