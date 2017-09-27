package com.dihanov.musiq.ui.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dimitar.dihanov on 9/27/2017.
 */

public class TopArtistAdapter extends ArrayAdapter<Artist> {
    private static final int ARTIST_IMAGE_LARGE = 4;
    private Context context;
    private List<Artist> topArtist;
    private int resourceId;

    public TopArtistAdapter(@NonNull Context context, int resource, ArrayList<Artist> data) {
        super(context, resource, data);
        this.topArtist = data;
        this.context = context;
        this.resourceId = resource;
    }


    public class MyViewHolder{
        public ImageView artist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        MyViewHolder holder;

        if (row == null) {
            row = LayoutInflater.from(parent.getContext())
                    .inflate(resourceId, parent, false);
            holder = new MyViewHolder();
            holder.artist = (ImageView) row.findViewById(R.id.top_artist_image);
            row.setTag(holder);
        } else {
            holder = (MyViewHolder) row.getTag();
        }


        Artist artist = topArtist.get(position);
        Glide.with(context)
                .load(artist.getImage().get(ARTIST_IMAGE_LARGE).getText())
                .crossFade(2000)
                .into(holder.artist);

        return row;
    }
}
