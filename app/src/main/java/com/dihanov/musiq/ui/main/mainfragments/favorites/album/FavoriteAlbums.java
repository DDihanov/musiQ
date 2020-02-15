package com.dihanov.musiq.ui.main.mainfragments.favorites.album;

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
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.Album;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.AlbumDetailsAdapter;
import com.dihanov.musiq.ui.main.AlbumDetailsPopupWindowManager;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.ui.main.mainfragments.ViewPagerCustomizedFragment;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dimitar.dihanov on 11/2/2017.
 */

public class FavoriteAlbums extends ViewPagerCustomizedFragment implements FavoriteAlbumsContract.View, AbstractAdapter.OnItemClickedListener<Album> {
    public static final String TITLE = "favorite albums";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    FavoriteAlbumsContract.Presenter favoriteFragmentsPresenter;

    @Inject
    AlbumDetailsPopupWindowManager albumDetailsPopupWindowManager;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    FavoritesManager favoritesManager;


    private MainActivity mainActivity;


    public static FavoriteAlbums newInstance() {
        Bundle args = new Bundle();
        FavoriteAlbums albumResultFragment = new FavoriteAlbums();
        albumResultFragment.setArguments(args);
        return albumResultFragment;
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
        this.favoriteFragmentsPresenter.takeView(this);
        this.favoriteFragmentsPresenter.loadFavoriteAlbums(
                userSettingsRepository.getSerializedFavoriteAlbums());

        return view;
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (HelperMethods.isTablet(mainActivity)){
            layoutManager = new GridLayoutManager(mainActivity, 3);
            recyclerView.addItemDecoration(new FavoriteAlbums.GridSpacingItemDecoration(3, HelperMethods.dpToPx(10, mainActivity), true));
        } else {
            layoutManager = new GridLayoutManager(mainActivity, 2);
            recyclerView.addItemDecoration(new FavoriteAlbums.GridSpacingItemDecoration(2, HelperMethods.dpToPx(10, mainActivity), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new AlbumDetailsAdapter(mainActivity,
                new ArrayList<>(),
                true, this,
                userSettingsRepository.getFavoriteAlbums(), favoritesManager));
    }

    @Override
    public Context getContext() {
        return this.mainActivity;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.favoriteFragmentsPresenter.leaveView();
    }

    @Override
    public void onResume() {
        super.onResume();
        //we need to refresh the artists just in case the user has modified the favorites
        loadFavoriteAlbums(
                userSettingsRepository.getSerializedFavoriteAlbums());
    }

    @Override
    public void onItemClicked(Album item) {
        favoriteFragmentsPresenter.fetchEntireAlbumInfo(item.getArtist().toString(), item.getName());
    }

    @Override
    public void showProgressBar() {
        mainActivity.showProgressBar();
    }

    @Override
    public void showAlbumDetails(Album fullAlbum) {
        albumDetailsPopupWindowManager.showAlbumDetails(mainActivity, fullAlbum);
    }

    @Override
    public void hideProgressBar() {
        mainActivity.hideProgressBar();
    }

    @Override
    public void setArtistAlbumsList(List<Album> albums) {
        ((AlbumDetailsAdapter) recyclerView.getAdapter()).setArtistAlbumsList(albums);
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

    private void loadFavoriteAlbums(Set<String> favorites) {
        //resetting the adapter
        recyclerView.setAdapter(new AlbumDetailsAdapter(this.mainActivity, new ArrayList<>(), true, this, userSettingsRepository.getFavoriteAlbums(), favoritesManager));
        favoriteFragmentsPresenter.loadFavoriteAlbums(favorites);
    }
}
