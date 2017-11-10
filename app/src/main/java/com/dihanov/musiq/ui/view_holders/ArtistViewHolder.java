package com.dihanov.musiq.ui.view_holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 11/10/2017.
 */

public class ArtistViewHolder extends RecyclerView.ViewHolder implements ClickableArtistViewHolder {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.count)
    TextView count;

    @BindView(R.id.thumbnail)
    ImageView thumbnail;

    public ArtistViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public ImageView getThumbnail() {
        return this.thumbnail;
    }

    public TextView getTitle() {
        return title;
    }

    public TextView getCount() {
        return count;
    }
}
