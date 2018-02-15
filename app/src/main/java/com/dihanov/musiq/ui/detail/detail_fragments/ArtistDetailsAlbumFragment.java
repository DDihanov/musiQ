package com.dihanov.musiq.ui.detail.detail_fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetailsActivity;
import com.dihanov.musiq.util.HelperMethods;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistDetailsAlbumFragment extends ArtistDetailsFragment {
    public static final String TITLE = "albums";

    private List<Album> artistAlbums;
    private String serializedAlbums;


    @Inject ArtistDetailsFragmentContract.Presenter artistDetailsFragmentPresenter;
    @BindView(R.id.albums_recycler_view) RecyclerView recyclerView;

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
        if(this.artistAlbums.size() == 0){
            this.serializedAlbums = artistDetailsActivity.getSerialiedAlbums();
            //very important not to use a genertic type token: e.g. new TypeToken<ArrayList<Album>>(){}.getType();
            //because this wont deserialize correctly, we need to use the model we created for this cause
            GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Album.class, new Album.DataStateDeserializer());
            this.artistAlbums = gsonBuilder.create().fromJson(serializedAlbums, TopArtistAlbums.class).getTopalbums().getAlbum();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(TITLE, this.serializedAlbums);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_artist_albums_recycler_view, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        this.artistDetailsFragmentPresenter.takeView(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        ArtistDetailsActivity artistDetailsActivity = (ArtistDetailsActivity) this.artistDetailsActivity;
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (HelperMethods.isTablet(artistDetailsActivity)){
            layoutManager = new GridLayoutManager(artistDetailsActivity, 3);
            recyclerView.addItemDecoration(new ArtistDetailsAlbumFragment.GridSpacingItemDecoration(3, HelperMethods.dpToPx(10, artistDetailsActivity), true));
        } else {
            layoutManager = new GridLayoutManager(artistDetailsActivity, 2);
            recyclerView.addItemDecoration(new ArtistDetailsAlbumFragment.GridSpacingItemDecoration(2, HelperMethods.dpToPx(10, artistDetailsActivity), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AlbumDetailsAdapter(artistDetailsActivity, this.artistAlbums, artistDetailsFragmentPresenter));
    }

    @Override
    public Context getContext() {
        return (Context)this.artistDetailsActivity;
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
