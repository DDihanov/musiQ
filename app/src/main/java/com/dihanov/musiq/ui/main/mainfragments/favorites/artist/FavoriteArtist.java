package com.dihanov.musiq.ui.main.mainfragments.favorites.artist;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.user.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.mainfragments.ViewPagerCustomizedFragment;
import com.dihanov.musiq.util.ActivityStarterWithIntentExtras;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class FavoriteArtist extends ViewPagerCustomizedFragment implements FavoriteArtistsContract.View, AbstractAdapter.OnItemClickedListener<Artist> {
    public static final String TITLE = "favorite artists";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Inject FavoriteArtistsContract.Presenter favoriteFragmentPresenter;

    @Inject
    ActivityStarterWithIntentExtras activityStarterWithIntentExtras;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    FavoritesManager favoritesManager;

    private MainActivity mainActivity;

    public static FavoriteArtist newInstance() {
        Bundle args = new Bundle();
        FavoriteArtist artistResultFragment = new FavoriteArtist();
        artistResultFragment.setArguments(args);
        return artistResultFragment;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mainActivity = (MainActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_search_fragment, container, false);
        ButterKnife.bind(this, view);


        //very important to call this - this enables us to use the below method(onCreateOptionsMenu), and allows us
        //to receive calls from MainActivity's onCreateOptionsMenu
        setHasOptionsMenu(true);

        initRecyclerView();

        this.favoriteFragmentPresenter.takeView(this);
        loadFavoriteArtists(
                userSettingsRepository.getFavoriteArtistsSerialized());

        return view;
    }

    private void loadFavoriteArtists(Set<String> stringSet) {
        //resetting the adapter
        recyclerView.setAdapter(new ArtistAdapter(this.mainActivity, new ArrayList<>(), true, this, userSettingsRepository.getFavoriteArtists(), favoritesManager));

        favoriteFragmentPresenter.loadFavoriteArtists(stringSet);
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (HelperMethods.isTablet(mainActivity)){
            layoutManager = new GridLayoutManager(mainActivity, 3);
            recyclerView.addItemDecoration(new FavoriteArtist.GridSpacingItemDecoration(3, HelperMethods.dpToPx(10, mainActivity), true));
        } else {
            layoutManager = new GridLayoutManager(mainActivity, 2);
            recyclerView.addItemDecoration(new FavoriteArtist.GridSpacingItemDecoration(2, HelperMethods.dpToPx(10, mainActivity), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ArtistAdapter(mainActivity,
                new ArrayList<>(),
                true, this,
                userSettingsRepository.getFavoriteArtists(), favoritesManager));
    }

    @Override
    public void onResume() {
        super.onResume();
        this.favoriteFragmentPresenter.loadFavoriteArtists(
                userSettingsRepository.getFavoriteArtistsSerialized());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.favoriteFragmentPresenter.leaveView();
    }


    @Override
    public void onItemClicked(Artist item) {
        favoriteFragmentPresenter.fetchArtist(item.getName());
    }

    @Override
    public void hideProgressBar() {
        mainActivity.hideProgressBar();
    }

    @Override
    public void showProgressBar() {
        mainActivity.showProgressBar();
    }

    @Override
    public void setArtistList(List<Artist> artists) {
        ((ArtistAdapter) recyclerView.getAdapter()).setArtistList(artists);
    }

    @Override
    public void startActivityWithExtras(HashMap<String, String> bundleExtra) {
        bundleExtra.put(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        activityStarterWithIntentExtras.startActivityWithExtras(bundleExtra, requireActivity(), ArtistDetails.class);
    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
