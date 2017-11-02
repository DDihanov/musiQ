package com.dihanov.musiq.interfaces;

import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public interface SpecificAlbumSearchable {
    void setClickListenerFetchEntireAlbumInfo(AlbumViewHolder viewHolder, String artistName, String albumName);
}
