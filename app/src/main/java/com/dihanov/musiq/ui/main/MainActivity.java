package com.dihanov.musiq.ui.main;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.dihanov.musiq.R;
import com.dihanov.musiq.util.KeyboardHelper;

import java.util.Collections;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dagger.android.AndroidInjection;
import dagger.android.HasFragmentInjector;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * Created by Dihanov on 9/16/2017.
 */

public class MainActivity extends DaggerAppCompatActivity implements MainActivityContract.View, HasFragmentInjector {
    @Inject
    MainActivityPresenter mainActivityPresenter;

    @BindView(R.id.search)
    EditText searchEditText;

    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initCollapsingToolbar();
        initRecyclerView();
        setSupportActionBar(toolbar);

        mainActivityPresenter.addOnAutoCompleteTextViewTextChangedObserver(recyclerView, searchEditText);
        mainActivityPresenter.addOnAutoCompleteTextViewItemClickedSubscriber(recyclerView);
        appBarLayout.addOnOffsetChangedListener(new OnOffsetChangedListener());
        appBarLayout.setExpanded(true);

    }

    private void initRecyclerView() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setAdapter(new ArtistAdapter(this, Collections.emptyList()));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainActivityPresenter.leaveView();
    }

    @Override
    public void showProgressBar() {
        this.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        this.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideKeyboard() {
        KeyboardHelper.hideKeyboard(this);
    }

    private void initCollapsingToolbar() {
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private class OnOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        boolean isShow = false;
        int scrollRange = -1;

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (scrollRange == -1) {
                scrollRange = appBarLayout.getTotalScrollRange();
            }
            if (scrollRange + verticalOffset == 0) {
                collapsingToolbar.setTitle(getString(R.string.app_name));
                isShow = true;
            } else if (isShow) {
                collapsingToolbar.setTitle(getString(R.string.app_name));
                isShow = false;
            }
        }
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

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
    //    public void getArtistExample() {
//        Single<Artist> artistInfo = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfo("Cher");
////        Call<Artist> call = mainActivityPresenter.lastFmApiClient.getLastFmApiService().getArtistInfoCall("Cher");
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////        call.enqueue(new Callback<Artist>() {
////            @Override
////            public void onResponse(Call<Artist> call, Response<Artist> response) {
////                Artist artist = response.body();
////                builder.setTitle(artist.getArtist().getName());
////                builder.setMessage(artist.getArtist().getBio().getContent());
////                builder.show();
////                System.out.println();
////            }
////
////            @Override
////            public void onFailure(Call<Artist> call, Throwable t) {
////            }
////        });
//
////        artistInfo
////                .subscribeOn(Schedulers.io())
////                .observeOn(AndroidSchedulers.mainThread())
////                .subscribe(artist -> {
////                            builder.setTitle(artist.getArtist().getName());
////                            builder.setMessage(artist.getArtist().getBio().getSummary());
////                            builder.show();});
////    }
}
