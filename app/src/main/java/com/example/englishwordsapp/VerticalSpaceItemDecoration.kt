package com.example.englishwordsapp

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(
    private val spaceBetween: Int,
    private val spaceEdge: Int
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        outRect.left = 0
        outRect.right = 0

        if (position == 0) {
            outRect.top = spaceEdge
        } else {
            outRect.top = spaceBetween
        }

        if (position == itemCount - 1) {
            outRect.bottom = spaceEdge
        } else {
            outRect.bottom = 0
        }
    }
}
