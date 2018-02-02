package com.dihanov.musiq.ui.listeners;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.dihanov.musiq.R;
import com.dihanov.musiq.di.app.App;
import com.dihanov.musiq.interfaces.ClickableArtistViewHolder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by dimitar.dihanov on 2/1/2018.
 */


public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
    private ClickableArtistViewHolder viewHolder;
    private String stringValue = "";
    private String key;


    //the key will be used to either add favorites to the artists or the albums category
    public MyMenuItemClickListener(String value, ClickableArtistViewHolder viewHolder, String key) {
        stringValue = value;
        this.viewHolder = viewHolder;
        this.key = key;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Set<String> stringSet = App.getSharedPreferences().getStringSet(key, new HashSet<>());

        switch (item.getItemId()) {
            case (R.id.action_add_favourite):
                stringSet.add(stringValue);
                App.getSharedPreferences().edit().putStringSet(key, stringSet).apply();
                viewHolder.setIsFavorited(true);
                return true;
            case (R.id.action_remove_favorite):
                Iterator<String> it = stringSet.iterator();
                while(it.hasNext()){
                    String entry = it.next();
                    if(entry.equals(stringValue)){
                        it.remove();
                        break;
                    }
                }
                App.getSharedPreferences().edit().putStringSet(key, stringSet).apply();
                viewHolder.setIsFavorited(false);
                return true;
            default:
                break;
        }

        return false;
    }
}