package com.aitgacem.budgeter.ui.components

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import kotlin.math.roundToInt

class DayDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.set(0, 0, 0, 0)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val mBounds = Rect()
        val mDivider = parent.context.getDrawable(R.drawable.item_divider)!!
        val left = parent.paddingLeft
        val right = parent.width - left

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            val bottom: Int = mBounds.bottom + child.translationY.roundToInt()
            val top: Int = bottom - parent.getChildAt(i).height
            mDivider.setBounds(left, top, right, bottom)
            mDivider.draw(canvas)
        }
        canvas.restore()
    }

}