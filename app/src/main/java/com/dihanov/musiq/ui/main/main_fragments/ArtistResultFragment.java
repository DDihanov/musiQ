package com.dihanov.musiq.ui.main.main_fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.Constants;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultFragment extends DaggerFragment implements ArtistResultFragmentContract.View {
    public static final String TITLE = "artists";

    @BindView(R.id.recycler_view) RecyclerView recyclerView;

    @Inject ArtistResultFragmentPresenter artistResultFragmentPresenter;

    private MainActivity mainActivity;

    public static ArtistResultFragment newInstance() {
        Bundle args = new Bundle();
        ArtistResultFragment artistResultFragment = new ArtistResultFragment();
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
        super.onCreateOptionsMenu(menu, inflater);
        artistResultFragmentPresenter.addOnSearchBarTextChangedListener(mainActivity, mainActivity.getSearchBar());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager layoutManager = null;
        //check if tablet --> 3 columns instead of 2;
        if (Constants.isTablet(mainActivity)){
            layoutManager = new GridLayoutManager(mainActivity, 3);
            recyclerView.addItemDecoration(new ArtistResultFragment.GridSpacingItemDecoration(3, Constants.dpToPx(10, mainActivity), true));
        } else {
            layoutManager = new GridLayoutManager(mainActivity, 2);
            recyclerView.addItemDecoration(new ArtistResultFragment.GridSpacingItemDecoration(2, Constants.dpToPx(10, mainActivity), true));
        }

        if(layoutManager == null){
            return;
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public Context getContext() {
        return this.mainActivity;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.artistResultFragmentPresenter.leaveView();
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
