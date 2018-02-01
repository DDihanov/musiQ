package com.dihanov.musiq.interfaces;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public interface ClickableArtistViewHolder{
    ImageView getThumbnail();

    TextView getTitle();

    TextView getCount();

    boolean getIsFavorited();

    void setIsFavorited(boolean isFavorited);


}
