package com.dihanov.musiq.di.modules;

import com.dihanov.musiq.ui.detail.fragment.DetailFragment;
import com.dihanov.musiq.ui.detail.fragment.DetailFragmentPresenter;
import com.dihanov.musiq.ui.detail.fragment.DetailFragmentView;

import dagger.Module;
import dagger.Provides;

/**
 * Created by mertsimsek on 02/06/2017.
 */
@Module
public class DetailFragmentModule {
    @Provides
    DetailFragmentView provideDetailView(DetailFragment detailFragment){
        return detailFragment;
    }

    @Provides
    DetailFragmentPresenter provideDetailPresenter(DetailFragmentView detailView){
        return new DetailFragmentPresenter(detailView);
    }
}
