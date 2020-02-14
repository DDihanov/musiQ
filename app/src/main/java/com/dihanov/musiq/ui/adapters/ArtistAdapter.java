package com.dihanov.musiq.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.view_holders.AbstractViewHolder;
import com.dihanov.musiq.ui.view_holders.ArtistViewHolder;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends AbstractAdapter {
    private static final long DELAY_IN_MILLIS = 500;
    private boolean isFavoriteType;
    private Context mainActivity;
    private List<Artist> artistList;
    private OnItemClickedListener<Artist> onItemClickedListener;

    public ArtistAdapter(Context context, List<Artist> albumList, OnItemClickedListener<Artist> onItemClickedListener) {
        this.mainActivity =context;
        this.artistList = albumList;
        this.onItemClickedListener = onItemClickedListener;
    }

    public ArtistAdapter(Context context, List<Artist> albumList, boolean isFavoriteType, OnItemClickedListener<Artist>  onItemClickedListener) {
        this.mainActivity = context;
        this.artistList = albumList;
        this.onItemClickedListener = onItemClickedListener;
        this.isFavoriteType = isFavoriteType;
    }

    @Override
    public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_card, parent, false);

        return new ArtistViewHolder(itemView);
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
        notifyDataSetChanged();
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
                .apply(new RequestOptions().placeholder(mainActivity.getApplicationContext().getResources()
                        .getIdentifier("ic_missing_image", "drawable", mainActivity
                                .getPackageName()))).transition(withCrossFade(1000))
                .into(holder.getThumbnail());

        ((ArtistViewHolder) holder).getOverflow().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.showPopupMenu(mainActivity, ((ArtistViewHolder) holder).getOverflow(),
                        ArtistAdapter.this, artist);
            }
        });

        RxView.clicks(holder.getThumbnail())
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(click -> onItemClickedListener.onItemClicked(artist));

        this.setIsFavorited(holder, Constants.FAVORITE_ARTISTS_KEY);
    }

    public void addArtist(Artist artist) {
        this.artistList.add(artist);
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }

    @Override
    public void remove(String key) {
        if(!isFavoriteType){
            return;
        }

        for (Iterator<Artist> i = artistList.listIterator(); i.hasNext(); ) {
            Artist artist = i.next();
            if(artist.getName().toLowerCase().equals(key.toLowerCase())){
                i.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }
}
