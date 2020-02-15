package com.dihanov.musiq.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.viewholders.AbstractViewHolder;
import com.dihanov.musiq.ui.viewholders.TopArtistsViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.FavoritesManager;
import com.jakewharton.rxbinding3.view.RxView;
import com.veinhorn.tagview.TagView;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by dimitar.dihanov on 9/27/2017.
 */

public class TopArtistAdapter extends AbstractAdapter {
    private boolean isFavoriteType = false;
    private Context context;
    private List<Artist> topArtist;
    private OnItemClickedListener<Artist> onItemClickedListener;

    public TopArtistAdapter(Context context, List<Artist> topArtist, OnItemClickedListener<Artist> onItemClickedListener, Set<String> favorites, FavoritesManager favoritesManager) {
        super(favorites, favoritesManager);
        this.context = context;
        this.topArtist = topArtist;
        this.onItemClickedListener = onItemClickedListener;
    }


    public TopArtistAdapter(Context context, List<Artist> topArtist, boolean isFavoriteType, OnItemClickedListener<Artist> onItemClickedListener, Set<String> favorites, FavoritesManager favoritesManager) {
        super(favorites, favoritesManager);
        this.context = context;
        this.topArtist = topArtist;
        this.isFavoriteType = isFavoriteType;
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public TopArtistsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.top_artist_viewholder, parent, false);
        return new TopArtistsViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        Artist artist = topArtist.get(position);
        Glide.with(context.getApplicationContext())
                .load(artist.getImage().get(Constants.IMAGE_XLARGE).getText())
                .apply(new RequestOptions().error(context.getApplicationContext().getResources()
                        .getIdentifier("ic_missing_image", "drawable", context
                                .getPackageName())))
                .transition(withCrossFade(500))
                .into(holder.getThumbnail());
        holder.getTitle().setText(artist.getName().toLowerCase());
        ((TagView)holder.getTitle()).setTagColor(Color.parseColor(context.getString(R.color.colorAccent)));
        ((TagView)holder.getTitle()).setTagCircleRadius(10f);
        RxView.clicks(holder.getThumbnail())
                .debounce(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> {
                    onItemClickedListener.onItemClicked(artist);
                });

        this.setIsFavorited(holder, Constants.FAVORITE_ARTISTS_KEY);
    }

    @Override
    public int getItemCount() {
        return this.topArtist.size();
    }


    @Override
    public void remove(String key) {
        if(!isFavoriteType){
            return;
        }

        for (Iterator<Artist> i = topArtist.listIterator(); i.hasNext(); ) {
            Artist artist = i.next();
            if(artist.getName().toLowerCase().equals(key.toLowerCase())){
                i.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }
}
