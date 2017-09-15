package com.dihanov.musiq.di.components;

import android.app.Application;

import com.dihanov.musiq.di.App;
import com.dihanov.musiq.di.modules.AppModule;
import com.dihanov.musiq.di.modules.NetworkModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import retrofit2.Retrofit;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        NetworkModule.class})
interface AppComponent {
    @Component.Builder
    interface Builder{
        @BindsInstance
        Builder application(Application application);
        AppComponent.build();
    }

    @Component.Builder
    interface Retrofit{
        @BindsInstance
        Retrofit retrofit(String baseUrl);
        Retrofit.build();
    }

    void inject(App app);
}
