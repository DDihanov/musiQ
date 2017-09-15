package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.service.RestClient;
import com.dihanov.musiq.ui.detail.DetailActivity;
import com.dihanov.musiq.ui.detail.DetailPresenter;
import com.dihanov.musiq.ui.detail.DetailPresenterImpl;
import com.dihanov.musiq.ui.detail.DetailView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Dimitar Dihanov on 15.9.2017 г..
 */

@Module
public class DetailActivityModule {
    @Provides
    DetailView provideDetailView(DetailActivity detailActivity){
        return detailActivity;
    }

    @Provides
    DetailPresenter provideMainPresenter(DetailView detailView, RestClient restClient){
        return new DetailPresenterImpl(detailView, restClient);
    }
}
