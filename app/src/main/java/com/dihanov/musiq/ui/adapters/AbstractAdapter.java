package com.dihanov.musiq.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.dihanov.musiq.ui.viewholders.AbstractViewHolder;
import com.dihanov.musiq.util.FavoritesManager;

import java.util.Set;

/**
 * Created by dimitar.dihanov on 2/1/2018.
 */

public abstract class AbstractAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    public interface OnItemClickedListener<T> {
        void onItemClicked(T item);
    }

    private Set<String> favorites;
    private FavoritesManager favoritesManager;

    public AbstractAdapter(Set<String> favorites, FavoritesManager favoritesManager) {
        this.favorites = favorites;
        this.favoritesManager = favoritesManager;
    }

    protected void setIsFavorited(AbstractViewHolder holder, String key){
        boolean isFavorited = false;
        //this automatically makes it so the button displays the correct setting
        for (String s : favorites) {
            if(s.toLowerCase().equals(holder.getTitle().getText().toString())){
                isFavorited = true;
                break;
            }
        }

        if (isFavorited){
            holder.setIsFavorited(true);
        } else {
            holder.setIsFavorited(false);
        }
    }

    public abstract void remove(String key);

    public FavoritesManager getFavoritesManager() {
        return favoritesManager;
    }
}
