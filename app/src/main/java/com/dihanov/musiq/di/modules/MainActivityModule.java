package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.main.old.MainActivity;
import com.dihanov.musiq.ui.main.old.MainPresenter;
import com.dihanov.musiq.ui.main.old.MainPresenterImpl;
import com.dihanov.musiq.ui.main.old.MainView;

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
    MainPresenter provideMainPresenter(MainView mainView, LastFmApiClient lastFmApiClient){
        return new MainPresenterImpl(mainView, lastFmApiClient);
    }
}
