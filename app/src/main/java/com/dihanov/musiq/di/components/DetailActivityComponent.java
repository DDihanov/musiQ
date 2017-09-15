package com.dihanov.musiq.di.components;

import com.dihanov.musiq.di.modules.DetailActivityModule;
import com.dihanov.musiq.ui.detail.DetailActivity;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Subcomponent(modules = DetailActivityModule.class)
public interface DetailActivityComponent extends AndroidInjector<DetailActivity>{

    @Subcomponent.Builder
    abstract class Builder extends AndroidInjector.Builder<DetailActivity>{}
}
