package com.dihanov.musiq.ui.main.mainfragments.artist;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dihanov.musiq.R;
import com.dihanov.musiq.data.repository.UserSettingsRepository;
import com.dihanov.musiq.models.Artist;
import com.dihanov.musiq.models.ArtistSearchResults;
import com.dihanov.musiq.ui.adapters.AbstractAdapter;
import com.dihanov.musiq.ui.adapters.ArtistAdapter;
import com.dihanov.musiq.ui.detail.ArtistDetails;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.ActivityStarterWithIntentExtras;
import com.dihanov.musiq.util.Constants;
import com.dihanov.musiq.util.FavoritesManager;
import com.dihanov.musiq.util.HelperMethods;
import com.jakewharton.rxbinding3.appcompat.RxSearchView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResult extends DaggerFragment implements ArtistResultContract.View, AbstractAdapter.OnItemClickedListener<Artist> {
    private static final long DELAY_IN_MILLIS = 2000;
    public static final String TITLE = "artists";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Inject ArtistResultContract.Presenter artistResultFragmentPresenter;

    @Inject
    ActivityStarterWithIntentExtras activityStarterWithIntentExtras;

    @Inject
    UserSettingsRepository userSettingsRepository;

    @Inject
    FavoritesManager favoritesManager;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MainActivity mainActivity;


    public static ArtistResult newInstance() {
        Bundle args = new Bundle();
        ArtistResult artistResult = new ArtistResult();
        artistResult.setArguments(args);
        return artistResult;
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

        this.artistResultFragmentPresenter.takeView(this);
        return view;
    }

    //Since the menu gets created in the onCreateOptionMenu method from MainActivity, we need to override this method, so we can set the listener,
    //in the exact moment the menu gets created. If we set the listener directly in MainActivity, the RecyclerView that we use in the listener method
    //will not have been initialized yet, and we will get a NullPointerException. However if we set the listener here, the recycler view will
    //already be initialized and in the same thread as the listener. It is important to set the listener in the same class(thread) where the recycler view
    //gets initialized.
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int orientation = this.getResources().getConfiguration().orientation;
        super.onCreateOptionsMenu(menu, inflater);
        addOnSearchBarTextChangedListener(mainActivity);
        if(!userSettingsRepository.isFirstLaunch()){
            mainActivity.getSearchBar().setIconified(false);
        } else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            mainActivity.getSearchBar().setIconified(false);
        }
    } 


    private void addOnSearchBarTextChangedListener(MainActivity mainActivity) {
        Disposable subscribe = RxSearchView.queryTextChanges(mainActivity.getSearchBar())
                .debounce(DELAY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .filter(s -> s.length() >= 2)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(s -> {
                    HelperMethods.checkConnection(mainActivity);
                })
                .doOnNext(s -> artistResultFragmentPresenter.searchForArtist(s.toString()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

        compositeDisposable.add(subscribe);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (HelperMethods.isTablet(mainActivity)){
            layoutManager = new GridLayoutManager(mainActivity, 3);
            recyclerView.addItemDecoration(new ArtistResult.GridSpacingItemDecoration(3, HelperMethods.dpToPx(10, mainActivity), true));
        } else {
            layoutManager = new GridLayoutManager(mainActivity, 2);
            recyclerView.addItemDecoration(new ArtistResult.GridSpacingItemDecoration(2, HelperMethods.dpToPx(10, mainActivity), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        this.artistResultFragmentPresenter.leaveView();
    }

    @Override
    public void hideProgressBar() {
        mainActivity.hideProgressBar();
    }

    @Override
    public void hideKeyboard() {
        mainActivity.hideKeyboard();
    }

    @Override
    public void setSearchResults(ArtistSearchResults artistSearchResults) {
        List<Artist> result = new ArrayList<>();
        result.addAll(artistSearchResults.getResults().getArtistmatches().getArtistMatches());
        if(result.isEmpty()){
            result = Collections.emptyList();
        }

        ArtistAdapter artistAdapter = new ArtistAdapter(mainActivity, result, this, userSettingsRepository.getFavoriteArtists(), favoritesManager);

        recyclerView.setAdapter(artistAdapter);
    }

    @Override
    public void showProgressBar() {
        mainActivity.showProgressBar();
    }

    @Override
    public void startActivityWithExtras(HashMap<String, String> bundleExtra) {
        bundleExtra.put(Constants.LAST_SEARCH, mainActivity.getSearchBar().getQuery().toString());
        activityStarterWithIntentExtras.startActivityWithExtras(bundleExtra, requireActivity(), ArtistDetails.class);
    }

    @Override
    public void onItemClicked(Artist item) {
        artistResultFragmentPresenter.fetchArtist(item.getName());
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
