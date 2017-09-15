package com.dihanov.musiq.di;

import android.app.Application;

import com.dihanov.musiq.config.Config;
import com.dihanov.musiq.di.components.DaggerNetworkComponent;
import com.dihanov.musiq.di.components.NetworkComponent;
import com.dihanov.musiq.di.modules.AppModule;
import com.dihanov.musiq.di.modules.NetworkModule;

/**
 * Created by Dimitar Dihanov on 14.9.2017 г..
 */

public class App extends Application {
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule(Config.LAST_FM_API_URL))
                .build();
    }
    public AppComponent getAppComponent() {
        return this.networkComponent;
    }
}
