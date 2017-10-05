package com.dihanov.musiq.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.util.Constants;
import com.veinhorn.tagview.TagView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 9/27/2017.
 */

public class TopArtistAdapter extends RecyclerView.Adapter<TopArtistAdapter.TopArtistsViewHolder> {
    private Context context;
    private List<Artist> topArtist;

    public TopArtistAdapter(Context context, List<Artist> topArtist) {
        this.context = context;
        this.topArtist = topArtist;
    }

    @Override
    public TopArtistsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.top_artist_viewholder, parent, false);
        return new TopArtistsViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(TopArtistsViewHolder holder, int position) {
        Artist artist = topArtist.get(position);
        Glide.with(context)
                .load(artist.getImage().get(Constants.IMAGE_XLARGE).getText())
                .crossFade(2000)
                .into(holder.artist);
        holder.name.setText(artist.getName().toLowerCase());
        holder.name.setTagColor(Color.parseColor(context.getString(R.color.colorAccent)));
        holder.name.setTagCircleRadius(10f);

    }

    @Override
    public int getItemCount() {
        return this.topArtist.size();
    }

    public class TopArtistsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.top_artist_name)
        TagView name;
        @BindView(R.id.top_artist_image)
        ImageView artist;

        public TopArtistsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
