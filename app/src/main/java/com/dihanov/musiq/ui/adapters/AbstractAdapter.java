package com.dihanov.musiq.ui.adapters;

import android.support.v7.widget.RecyclerView;

import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.ui.view_holders.AbstractViewHolder;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by dimitar.dihanov on 2/1/2018.
 */

public abstract class AbstractAdapter extends RecyclerView.Adapter<AbstractViewHolder> {
    public interface OnItemClickedListener<T> {
        void onItemClicked(T item);
    }

    protected void setIsFavorited(AbstractViewHolder holder, String key){
        boolean isFavorited = false;
        //this automatically makes it so the button displays the correct setting
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());
        for (String s : stringSet) {
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
}
