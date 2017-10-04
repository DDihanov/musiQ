package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.service.LastFmApiClient;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.ui.main.main_fragments.ArtistResultFragmentContract;
import com.dihanov.musiq.util.Constants;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistDetailsAlbumFragment extends DaggerFragment implements ArtistResultFragmentContract.View {
    public static final String TITLE = "albums";

    private List<Album> artistAlbums;
    private ArtistDetailsActivity artistDetailsActivity;

    @BindView(R.id.albums_recycler_view) RecyclerView recyclerView;
    @Inject LastFmApiClient lastFmApiClient;

    public ArtistDetailsAlbumFragment() {
        this.artistAlbums = new ArrayList<>();
    }

    public static ArtistDetailsAlbumFragment newInstance() {
        Bundle args = new Bundle();
        ArtistDetailsAlbumFragment artistDetailsAlbumFragment = new ArtistDetailsAlbumFragment();
        artistDetailsAlbumFragment.setArguments(args);
        return artistDetailsAlbumFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.artistDetailsActivity = (ArtistDetailsActivity)getActivity();
        this.artistAlbums = artistDetailsActivity.getAlbums();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_artist_albums_recycler_view, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (Constants.isTablet(artistDetailsActivity)){
            layoutManager = new GridLayoutManager(artistDetailsActivity, 3);
            recyclerView.addItemDecoration(new ArtistDetailsAlbumFragment.GridSpacingItemDecoration(3, dpToPx(10), true));
        } else {
            layoutManager = new GridLayoutManager(artistDetailsActivity, 2);
            recyclerView.addItemDecoration(new ArtistDetailsAlbumFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new ArtistDetailsAlbumAdapter(this.artistDetailsActivity, this.artistAlbums, lastFmApiClient));
    }

    @Override
    public Context getContext() {
        return this.artistDetailsActivity;
    }

    //dp to pixel
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public RecyclerView getRecyclerView() {
        return this.recyclerView;
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
