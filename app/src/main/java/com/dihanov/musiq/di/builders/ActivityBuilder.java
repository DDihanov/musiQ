package com.dihanov.musiq.di;

import android.app.Activity;

import com.dihanov.musiq.ui.MainActivity;
import com.dihanov.musiq.di.components.MainActivityComponent;

import dagger.Binds;
import dagger.Module;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjector;
import dagger.multibindings.IntoMap;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public abstract class ActivityBuilder {
    @Binds
    @IntoMap
    @ActivityKey(MainActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindMainActivity(MainActivityComponent.Builder builder);
}
