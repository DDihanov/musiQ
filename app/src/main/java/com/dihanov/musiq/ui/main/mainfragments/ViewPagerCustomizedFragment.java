package com.dihanov.musiq.ui.main.mainfragments;

import android.content.Context;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.ViewPagerAdapter;
import com.dihanov.musiq.util.KeyboardHelper;

import dagger.android.support.DaggerFragment;

/**
 * Created by dimitar.dihanov on 2/26/2018.
 */

public class ViewPagerCustomizedFragment extends DaggerFragment {
    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity)getActivity();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.getItem(0);
        SearchView search = (SearchView)item.getActionView();
        search.setIconified(true);
        KeyboardHelper.hideKeyboard(getActivity());
        search.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.setViewPagerSelection(ViewPagerAdapter.getArtistResultCase());
                KeyboardHelper.hideKeyboard(mainActivity);
            }
        });
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }
}
