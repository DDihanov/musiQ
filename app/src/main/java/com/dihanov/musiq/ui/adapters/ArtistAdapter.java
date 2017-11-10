package com.dihanov.musiq.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.main_fragments.artist.ArtistResultFragmentPresenter;
import com.dihanov.musiq.ui.view_holders.ArtistViewHolder;
import com.dihanov.musiq.util.Constants;

import java.util.List;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistViewHolder> {
    private MainActivity mainActivity;
    private List<Artist> artistList;
    private ArtistResultFragmentPresenter artistResultFragmentPresenter;

    public ArtistAdapter(MainActivity context, List<Artist> albumList, ArtistResultFragmentPresenter artistResultFragmentPresenter) {
        this.mainActivity = context;
        this.artistList = albumList;
        this.artistResultFragmentPresenter = artistResultFragmentPresenter;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new ArtistViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistViewHolder holder, int position) {
        if (artistList.isEmpty()) {
            return;
        }
        Artist artist = artistList.get(position);

        holder.getTitle().setText(artist.getName().toLowerCase());
        holder.getCount().setText(Constants.formatNumberWithSeperator(artist.getListeners()) + " listeners");

        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(artist.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.getThumbnail());

        this.artistResultFragmentPresenter.addOnArtistResultClickedListener(holder, artist.getName());
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }
}
