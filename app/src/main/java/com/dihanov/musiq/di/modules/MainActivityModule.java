package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.service.LastFmApiService;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.MainPresenter;
import com.dihanov.musiq.ui.main.MainPresenterImpl;
import com.dihanov.musiq.ui.main.MainView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public class MainActivityModule {
    @Provides
    MainView provideMainView(MainActivity mainActivity){
        return mainActivity;
    }

    @Provides
    MainPresenter provideMainPresenter(MainView mainView, LastFmApiService apiService){
        return new MainPresenterImpl(mainView, apiService);
    }
}
