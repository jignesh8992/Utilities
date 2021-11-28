package com.example.jdrodi.utilities

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: Jignesh N Patel
 * @date: 17-Feb-2021 9:22 PM
 */
class LinearSpaceItemDecoration(private val horizontalSpacing: Int, private val verticalSpacing: Int) : ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = horizontalSpacing
        if (parent.getChildLayoutPosition(view) != 0) {
            outRect.left = horizontalSpacing
        }
        //  outRect.top = verticalSpacing;
        outRect.bottom = verticalSpacing
    }

}