package com.dihanov.musiq.di.app;

import android.app.Application;

import com.dihanov.musiq.di.builders.ActivityBuilder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityBuilder.class})
public interface AppComponent {
    @Component.Builder
    interface Builder{
        AppComponent build();
        @BindsInstance Builder application(Application application);
        @BindsInstance Builder networkModule(String baseUrl);
    }

    void inject(App app);
}
