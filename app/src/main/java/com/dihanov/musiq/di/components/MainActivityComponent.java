package com.dihanov.musiq.di.components;

import com.dihanov.musiq.di.modules.MainActivityModule;
import com.dihanov.musiq.ui.main.old.MainActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Subcomponent(modules = MainActivityModule.class)
public interface MainActivityComponent extends AndroidInjector<MainActivity>{
    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<MainActivity>{}
}
