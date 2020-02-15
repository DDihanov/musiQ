package com.dihanov.musiq.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.Favoritable;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.listeners.OverflowClickListener;
import com.dihanov.musiq.util.Constants;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class AlbumViewHolder extends AbstractViewHolder {
    @BindView(R.id.album_title)
    TextView title;

    @BindView(R.id.album_song_count)
    TextView count;

    @BindView(R.id.album_thumbnail)
    ImageView thumbnail;

    @BindView(R.id.album_overflow)
    ImageView overflow;

    public AlbumViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    @Override
    public void showPopupMenu(Context context, View view, AbstractAdapter adapter, Favoritable favoritable)  {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_artist, popup.getMenu());
        Menu menu = popup.getMenu();

        if(!this.getIsFavorited()){
            menu.findItem(R.id.action_remove_favorite).setVisible(false);
            menu.findItem(R.id.action_add_favourite).setVisible(true);
        } else {
            menu.findItem(R.id.action_remove_favorite).setVisible(true);
            menu.findItem(R.id.action_add_favourite).setVisible(false);
        }


        popup.setOnMenuItemClickListener(new OverflowClickListener(favoritable.getName().toLowerCase(), new Gson().toJson(favoritable, Album.class),
                this, Constants.FAVORITE_ALBUMS_KEY,
                adapter));
        popup.show();
    }

    @Override
    public TextView getTitle() {
        return title;
    }

    public TextView getCount() {
        return count;
    }

    public ImageView getThumbnail() {
        return thumbnail;
    }

    public ImageView getOverflow(){
        return this.overflow;
    }
}
