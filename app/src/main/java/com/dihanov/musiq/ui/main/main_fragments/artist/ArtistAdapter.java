package com.dihanov.musiq.ui.main.main_fragments.artist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dimitar Dihanov on 19.9.2017 г..
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder> {
    private MainActivity mainActivity;
    private List<Artist> artistList;
    private ArtistResultFragmentPresenter artistResultFragmentPresenter;

    public ArtistAdapter(MainActivity context, List<Artist> albumList, ArtistResultFragmentPresenter artistResultFragmentPresenter) {
        this.mainActivity = context;
        this.artistList = albumList;
        this.artistResultFragmentPresenter = artistResultFragmentPresenter;
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

        holder.title.setText(artist.getName().toLowerCase());
        holder.count.setText(Constants.formatNumberWithSeperator(artist.getListeners()) + " listeners");

        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(artist.getImage().get(Constants.IMAGE_LARGE).getText())
                .into(holder.thumbnail);

        this.artistResultFragmentPresenter.addOnArtistResultClickedListener(holder, artist.getName());
    }

    @Override
    public int getItemCount() {
        return this.artistList.size();
    }
}
