package com.dihanov.musiq.ui.main.main_fragments;

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
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.ui.main.MainActivity;
import com.dihanov.musiq.util.KeyboardHelper;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.support.DaggerFragment;

/**
 * Created by Dimitar Dihanov on 20.9.2017 г..
 */

public class ArtistResultFragment extends DaggerFragment implements ArtistResultFragmentContract.View {
    public static final String TITLE = "Artists";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Inject
    ArtistResultFragmentPresenter artistResultFragmentPresenter;

    private MainActivity mainActivity;

    public static ArtistResultFragment newInstance() {
        Bundle args = new Bundle();
        ArtistResultFragment artistResultFragment = new ArtistResultFragment();
        artistResultFragment.setArguments(args);
        return artistResultFragment;
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
        initRecyclerView();

        this.artistResultFragmentPresenter.takeView(this);
        this.artistResultFragmentPresenter.addOnTextViewTextChangedObserver(mainActivity, mainActivity.getSearchBar());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mainActivity, 2);
        recyclerView.addItemDecoration(new ArtistResultFragment.GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(new ArtistAdapter(mainActivity, Collections.emptyList()));
    }

    @Override
    public Context getContext() {
        return this.mainActivity;
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
