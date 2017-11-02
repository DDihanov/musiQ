package com.dihanov.musiq.ui.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.Constants;

import java.util.List;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class AlbumDetailsAdapter extends RecyclerView.Adapter<AlbumViewHolder> {
    private Activity context;
    private List<Album> artistAlbumsList;
    private SpecificAlbumSearchable presenter;

    public AlbumDetailsAdapter(Activity context, List<Album> artistAlbumsList, SpecificAlbumSearchable presenter) {
        this.context = context;
        this.artistAlbumsList = artistAlbumsList;
        this.presenter = presenter;
    }


    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_artist_albums, parent, false);

        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
        if (artistAlbumsList.isEmpty()) {
            return;
        }
        Album album = artistAlbumsList.get(position);

        holder.title.setText(album.getName().toLowerCase());
        if(album.getPlaycount() != null){
            holder.count.setText(Constants.formatNumberWithSeperator((int)album.getPlaycount()) + " plays");
        }

        // loading album cover using Glide library
        Glide.with(context)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.thumbnail);

        this.presenter.setClickListenerFetchEntireAlbumInfo(holder, album.getArtist().toString(), album.getName());
    }

    @Override
    public int getItemCount() {
        return this.artistAlbumsList.size();
    }

}
