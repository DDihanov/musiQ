package com.dihanov.musiq.ui.main.main_fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private final int ARTIST_IMAGE_LARGE = 3;
    private Context context;
    private List<Artist> artistList;

    public ArtistAdapter(Context context, List<Artist> albumList) {
        this.context = context;
        this.artistList = albumList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        public TextView title;

        @BindView(R.id.count)
        public TextView count;

        @BindView(R.id.thumbnail)
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (artistList.isEmpty()) {
            return;
        }
        Artist artist = artistList.get(position);
        holder.title.setText(artist.getName());
        holder.count.setText(artist.getListeners() + " listeners");

        // loading album cover using Glide library
        Glide.with(context)
                .load(artist.getImage().get(ARTIST_IMAGE_LARGE).getText())
                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showArtistDetails(artistList.get(view.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }

    private void showArtistDetails(Artist artist) {
        Intent showArtistDetailsIntent = new Intent(context, ArtistDetailsActivity.class);
        showArtistDetailsIntent.putExtra(Constants.ARTIST, new Gson().toJson(artist));
    }
}
