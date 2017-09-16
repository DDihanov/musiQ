package com.dihanov.musiq.ui.main;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dihanov.musiq.R;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DaggerActivity;
import dagger.android.DispatchingAndroidInjector;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerActivity implements MainActivityContract.View {
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentInjector;

    @Inject
    MainActivityPresenter mainActivityPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public AndroidInjector<Fragment> fragmentInjector() {
        return fragmentInjector;
    }
}
