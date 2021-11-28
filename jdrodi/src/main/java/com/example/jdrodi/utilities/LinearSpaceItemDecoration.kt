package com.example.jdrodi.utilities;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: Jignesh N Patel
 * @date: 17-Feb-2021 9:22 PM
 */
public class LinearSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int horizontalSpacing = 0;
    private int verticalSpacing = 0;

    public LinearSpaceItemDecoration(int horizontalSpacing, int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = horizontalSpacing;
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.left = horizontalSpacing;
        }
      //  outRect.top = verticalSpacing;
        outRect.bottom = verticalSpacing;
    }
}
