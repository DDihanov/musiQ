package com.dihanov.musiq.ui.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.veinhorn.tagview.TagView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public class TopArtistsViewHolder extends RecyclerView.ViewHolder implements ClickableArtistViewHolder {
    @BindView(R.id.top_artist_name)
    TagView name;
    @BindView(R.id.top_artist_image)
    ImageView artist;

    public TopArtistsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public ImageView getThumbnail() {
        return artist;
    }

    public TagView getName() {
        return name;
    }
}

