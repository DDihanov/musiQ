package com.dihanov.musiq.ui.detail.detail_fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistDetailsAlbumAdapter extends RecyclerView.Adapter<ArtistDetailsAlbumAdapter.MyViewHolder> {
    private ArtistDetailsActivity detailsActivity;
    private List<Album> artistAlbumsList;
    private ArtistDetailsFragmentPresenter artistDetailsFragmentPresenter;

    public ArtistDetailsAlbumAdapter(ArtistDetailsActivity context, List<Album> artistAlbumsList, ArtistDetailsFragmentPresenter artistDetailsFragmentPresenter) {
        this.detailsActivity = context;
        this.artistAlbumsList = artistAlbumsList;
        this.artistDetailsFragmentPresenter = artistDetailsFragmentPresenter;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.album_title)
        public TextView title;

        @BindView(R.id.album_song_count)
        public TextView count;

        @BindView(R.id.album_thumbnail)
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_artist_albums, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (artistAlbumsList.isEmpty()) {
            return;
        }
        Album album = artistAlbumsList.get(position);

        holder.title.setText(album.getName().toLowerCase());
        holder.count.setText(Constants.formatNumberWithSeperator((int)album.getPlaycount()) + " plays");

        // loading album cover using Glide library
        Glide.with(detailsActivity)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.thumbnail);

        this.artistDetailsFragmentPresenter.setClickListenerFetchEntireAlbumInfo(album, holder);
    }

    @Override
    public int getItemCount() {
        return this.artistAlbumsList.size();
    }

}
