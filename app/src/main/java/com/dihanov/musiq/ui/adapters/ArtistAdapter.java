package com.dihanov.musiq.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.SpecificArtistSearchable;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.view_holders.AbstractViewHolder;
import com.dihanov.musiq.ui.view_holders.ArtistViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import java.util.List;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends AbstractAdapter {
    private Context mainActivity;
    private List<Artist> artistList;
    private SpecificArtistSearchable specificArtistSearchable;

    public ArtistAdapter(Context context, List<Artist> albumList, SpecificArtistSearchable specificArtistSearchable) {
        this.mainActivity = context;
        this.artistList = albumList;
        this.specificArtistSearchable = specificArtistSearchable;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (artistList.isEmpty()) {
            return;
        }
        Artist artist = artistList.get(position);

        holder.getTitle().setText(artist.getName().toLowerCase());

        if (artist.getListeners() != null) {
            holder.getCount().setText(HelperMethods.formatNumberWithSeperator(artist.getListeners()) + " listeners");
        }


        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(artist.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.getThumbnail());

        ((ArtistViewHolder) holder).getOverflow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopupMenu(mainActivity, ((ArtistViewHolder) holder).getOverflow());
            }
        });

        this.specificArtistSearchable.addOnArtistResultClickedListener(holder, artist.getName());
        this.setIsFavorited(holder, Constants.FAVORITE_ARTISTS_KEY);

    }

    public void addArtist(Artist artist) {
        this.artistList.add(artist);
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }

}
