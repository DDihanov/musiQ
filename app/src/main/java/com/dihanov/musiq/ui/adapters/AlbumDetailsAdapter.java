package com.dihanov.musiq.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.viewholders.AbstractViewHolder;
import com.dihanov.musiq.ui.viewholders.AlbumViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class AlbumDetailsAdapter extends AbstractAdapter {
    private boolean isFavoriteType;
    private Context context;
    private List<Album> artistAlbumsList;

    private OnItemClickedListener<Album> onItemClickedListener;

    public AlbumDetailsAdapter(Context context, List<Album> artistAlbumsList, OnItemClickedListener onItemClickedListener, Set<String> favorites, FavoritesManager favoritesManager) {
        super(favorites, favoritesManager);
        this.context = context;
        this.artistAlbumsList = artistAlbumsList;
        this.onItemClickedListener = onItemClickedListener;
    }

    public AlbumDetailsAdapter(Context context, List<Album> artistAlbumsList, boolean isFavoriteType, OnItemClickedListener onItemClickedListener, Set<String> favorites, FavoritesManager favoritesManager) {
        super(favorites, favoritesManager);
        this.context = context;
        this.artistAlbumsList = artistAlbumsList;
        this.onItemClickedListener = onItemClickedListener;
        this.isFavoriteType = isFavoriteType;
    }

    public void setArtistAlbumsList(List<Album> artistAlbumsList) {
        this.artistAlbumsList = artistAlbumsList;
        notifyDataSetChanged();
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
            if (album.getPlaycount() instanceof Double){
                holder.getCount().setText(HelperMethods.formatNumberWithSeperator((double)album.getPlaycount()) + " plays");
            } else {
                holder.getCount().setText(HelperMethods.formatNumberWithSeperator((int)album.getPlaycount()) + " plays");
            }
        }

        // loading album cover using Glide library
        Glide.with(context)
                .load(album.getImage().get(Constants.IMAGE_LARGE).getText())
                .apply(new RequestOptions().placeholder(context.getApplicationContext().getResources()
                        .getIdentifier("ic_missing_image", "drawable", context
                                .getPackageName())))
                .transition(withCrossFade(500))
                .into(holder.getThumbnail());

        ((AlbumViewHolder)holder).getOverflow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopupMenu(context, ((AlbumViewHolder)holder).getOverflow(),
                        AlbumDetailsAdapter.this, album);
            }
        });

        ((AlbumViewHolder) holder).getThumbnail().setOnClickListener(__->
                onItemClickedListener.onItemClicked(album));

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
