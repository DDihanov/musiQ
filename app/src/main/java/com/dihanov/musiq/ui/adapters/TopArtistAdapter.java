package com.dihanov.musiq.ui.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.BaseView;
import com.dihanov.musiq.ui.main.MainPresenter;
import com.dihanov.musiq.ui.view_holders.AbstractViewHolder;
import com.dihanov.musiq.ui.view_holders.TopArtistsViewHolder;
import com.dihanov.musiq.util.Constants;
import com.veinhorn.tagview.TagView;

import java.util.Iterator;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by dimitar.dihanov on 9/27/2017.
 */

public class TopArtistAdapter extends AbstractAdapter {
    private boolean isFavoriteType = false;
    private MainPresenter mainPresenter;
    private Context context;
    private List<Artist> topArtist;

    public TopArtistAdapter(BaseView<?> context, List<Artist> topArtist, MainPresenter mainPresenter) {
        this.context = (Activity)context;
        this.topArtist = topArtist;
        this.mainPresenter = mainPresenter;
    }


    public TopArtistAdapter(BaseView<?> context, List<Artist> topArtist, MainPresenter mainPresenter, boolean isFavoriteType) {
        this.context = (Activity)context;
        this.topArtist = topArtist;
        this.mainPresenter = mainPresenter;
        this.isFavoriteType = isFavoriteType;
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
                .apply(new RequestOptions().error(App.getAppContext().getResources()
                        .getIdentifier("ic_missing_image", "drawable", App.getAppContext()
                                .getPackageName())))
                .transition(withCrossFade(2000))
                .into(holder.getThumbnail());
        holder.getTitle().setText(artist.getName().toLowerCase());
        ((TagView)holder.getTitle()).setTagColor(Color.parseColor(context.getString(R.color.colorAccent)));
        ((TagView)holder.getTitle()).setTagCircleRadius(10f);
        holder.getThumbnail().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainPresenter.addOnArtistResultClickedListener(holder, artist.getName());
            }
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
