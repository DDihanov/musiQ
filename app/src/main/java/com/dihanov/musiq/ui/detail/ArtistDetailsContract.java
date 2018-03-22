package com.dihanov.musiq.ui.detail;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import com.dihanov.musiq.interfaces.MainViewFunctionable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BasePresenter;
import com.dihanov.musiq.ui.BaseView;

/**
 * Created by dimitar.dihanov on 9/29/2017.
 */

public interface ArtistDetailsContract {
    interface View extends BaseView<Presenter>, MainViewFunctionable {
        String getArtistBiography();

        DrawerLayout getDrawerLayout();

        NavigationView getNavigationView();

        String getSerialiedAlbums();

        void setArtist(Artist artist);

        Artist getArtist();

        ArtistDetails getDetailActivity();

        Context getContext();
    }

    interface Presenter extends BasePresenter<View> {
        void setOnDrawerOpenedListener(ArtistDetailsContract.View detailsActivity);
    }
}
