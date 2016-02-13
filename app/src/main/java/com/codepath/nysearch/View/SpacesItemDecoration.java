package com.codepath.nysearch.View;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by JaneChung on 2/13/16.
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    public SpacesItemDecoration(int space) {
        this.mSpace = space;
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

    }
}
