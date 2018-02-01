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
    protected void setIsFavorited(AbstractViewHolder holder, String key){
        //this automatically makes it so the button displays the correct setting
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());
        for (String s : stringSet) {
            if(s.equals(holder.getTitle().getText().toString())){
                holder.setIsFavorited(true);
                break;
            }
        }
    }
}
