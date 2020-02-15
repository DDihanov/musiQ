package com.dihanov.musiq.ui.main;

import android.content.Context;
import android.util.DisplayMetrics;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by dimitar.dihanov on 10/6/2017.
 */

public class CustomGridLayoutManager extends GridLayoutManager {
    private Context context;

    public CustomGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.context = context;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(context) {

            private static final float SPEED = 4000f;// Change this value (default=25f)

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return SPEED / displayMetrics.densityDpi;
            }

        };
        smoothScroller.setTargetPosition(position);
        startSmoothScroll(smoothScroller);
    }
}
