package com.dihanov.musiq.ui.listeners;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.dihanov.musiq.R;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.util.FavoritesManager;

/**
 * Created by dimitar.dihanov on 2/1/2018.
 */


public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
    private ClickableArtistViewHolder viewHolder;
    private String stringValue = "";
    private String key;
    private AbstractAdapter adapterToRefresh;


    //the key will be used to either add favorites to the artists or the albums category
    public MyMenuItemClickListener(String value, ClickableArtistViewHolder viewHolder, String key, AbstractAdapter adapter) {
        stringValue = value;
        this.viewHolder = viewHolder;
        this.key = key;
        this.adapterToRefresh = adapter;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_add_favourite):
                FavoritesManager.addToFavorites(key, stringValue);
                viewHolder.setIsFavorited(true);
                return true;
            case (R.id.action_remove_favorite):
                FavoritesManager.removeFromFavorites(key, stringValue);
                viewHolder.setIsFavorited(false);
                adapterToRefresh.remove(stringValue);
                return true;
            default:
                break;
        }

        return false;
    }
}