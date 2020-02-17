package com.dihanov.musiq.ui.detail.detailfragments;

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
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.models.TopArtistAlbums;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindowManager;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistSpecificsAlbum extends ArtistSpecifics implements AbstractAdapter.OnItemClickedListener<Album> {
    public static final String TITLE = "artist top albums";

    private List<Album> artistAlbums;
    private String serializedAlbums;

    private Disposable disposable;

    @Inject
    ArtistSpecificsContract.Presenter artistDetailsFragmentPresenter;

    @BindView(R.id.albums_recycler_view)
    RecyclerView recyclerView;

    @Inject
    AlbumDetailsPopupWindowManager albumDetailsPopupWindowManager;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    FavoritesManager favoritesManager;

    public ArtistSpecificsAlbum() {
        this.artistAlbums = new ArrayList<>();
    }

    public static ArtistSpecificsAlbum newInstance() {
        Bundle args = new Bundle();
        ArtistSpecificsAlbum artistDetailsAlbumFragment = new ArtistSpecificsAlbum();
        artistDetailsAlbumFragment.setArguments(args);
        return artistDetailsAlbumFragment;
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

        initAlbums();
        this.artistDetailsFragmentPresenter.takeView(this);
        return view;
    }

    private void initAlbums() {
        disposable = Single.just(artistAlbums)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSuccess(__ -> {
                    if (artistAlbums.size() == 0) {
                        serializedAlbums = artistDetailsActivity.getSerialiedAlbums();
                        //very important not to use a genertic type token: e.g. new TypeToken<ArrayList<Album>>(){}.getType();
                        //because this wont deserialize correctly, we need to use the model we created for this cause
                        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Album.class, new Album.DataStateDeserializer());
                        artistAlbums = gsonBuilder.create().fromJson(serializedAlbums, TopArtistAlbums.class).getTopalbums().getAlbum();
                    } else {
                        artistAlbums = new ArrayList<>();
                    }
                    initRecyclerView();
                })
                .subscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        ArtistDetails artistDetails =  this.artistDetailsActivity;
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (HelperMethods.isTablet(artistDetails)) {
            layoutManager = new GridLayoutManager(artistDetails, 3);
            recyclerView.addItemDecoration(new ArtistSpecificsAlbum.GridSpacingItemDecoration(3, HelperMethods.dpToPx(10, artistDetails), true));
        } else {
            layoutManager = new GridLayoutManager(artistDetails, 2);
            recyclerView.addItemDecoration(new ArtistSpecificsAlbum.GridSpacingItemDecoration(2, HelperMethods.dpToPx(10, artistDetails), true));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AlbumDetailsAdapter(artistDetails, this.artistAlbums, this, userSettingsRepository.getFavoriteAlbums(), favoritesManager));
    }

    @Override
    public void showAlbumDetails(Album fullAlbum) {
        albumDetailsPopupWindowManager.showAlbumDetails(requireActivity(), fullAlbum);
    }

    @Override
    public void onItemClicked(Album item) {
        artistDetailsFragmentPresenter.fetchEntireAlbumInfo(item.getArtist().toString(), item.getName());
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
