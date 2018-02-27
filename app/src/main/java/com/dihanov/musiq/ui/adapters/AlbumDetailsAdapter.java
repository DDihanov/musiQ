package com.dihanov.musiq.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.SpecificAlbumSearchable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.view_holders.AbstractViewHolder;
import com.dihanov.musiq.ui.view_holders.AlbumViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class AlbumDetailsAdapter extends AbstractAdapter {
    private boolean isFavoriteType;
    private Activity context;
    private List<Album> artistAlbumsList;
    private SpecificAlbumSearchable presenter;

    public AlbumDetailsAdapter(BaseView<?> context, List<Album> artistAlbumsList, SpecificAlbumSearchable presenter) {
        this.context = (Activity)context;
        this.artistAlbumsList = artistAlbumsList;
        this.presenter = presenter;
    }

    public AlbumDetailsAdapter(BaseView<?> context, List<Album> artistAlbumsList, SpecificAlbumSearchable presenter, boolean isFavoriteType) {
        this.context = (Activity)context;
        this.artistAlbumsList = artistAlbumsList;
        this.presenter = presenter;
        this.isFavoriteType = isFavoriteType;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.detail_artist_albums, parent, false);

        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (artistAlbumsList.isEmpty()) {
            return;
        }
        Album album = artistAlbumsList.get(position);

        holder.getTitle().setText(album.getName().toLowerCase());
        if(album.getPlaycount() != null){
            holder.getCount().setText(HelperMethods.formatNumberWithSeperator((int)album.getPlaycount()) + " plays");
        }

        // loading album cover using Glide library
        Glide.with(context)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.getThumbnail());

        ((AlbumViewHolder)holder).getOverflow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopupMenu(context, ((AlbumViewHolder)holder).getOverflow(),
                        AlbumDetailsAdapter.this);
            }
        });

        this.presenter.setClickListenerFetchEntireAlbumInfo((AlbumViewHolder)holder, album.getArtist().toString(), album.getName());
        this.setIsFavorited(holder, Constants.FAVORITE_ALBUMS_KEY);
    }

    public void addAlbum(Album album){
        this.artistAlbumsList.add(album);
    }

    @Override
    public int getItemCount() {
        return this.artistAlbumsList.size();
    }

    @Override
    public void remove(String key) {
        if(!isFavoriteType){
            return;
        }

        for (Iterator<Album> i = artistAlbumsList.listIterator(); i.hasNext(); ) {
            Album album = i.next();
            if(album.getName().toLowerCase().equals(key.toLowerCase())){
                i.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }
}
