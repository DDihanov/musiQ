package com.dihanov.musiq.di.app;

import android.app.Application;

import com.dihanov.musiq.App;
import com.dihanov.musiq.di.builders.BindingModule;
import com.dihanov.musiq.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        BindingModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance Builder application(Application application);
        Builder networkModule(NetworkModule networkModule);
        AppComponent build();
    }

    void inject(App app);
}
