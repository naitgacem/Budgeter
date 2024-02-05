package com.aitgacem.budgeter.ui.components

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.aitgacem.budgeter.R
import kotlin.math.roundToInt

class DayDecorator : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        val type = parent.getChildViewHolder(view).itemViewType
        return when (parent.getChildViewHolder(view)) {
            is HeaderViewHolder -> {
                outRect.set(0, 0, 0, 0)
            }

            else -> {
                outRect.set(0, 20, 0, 0)
            }
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        canvas.save()
        val mBounds = Rect()
        val contour = AppCompatResources.getDrawable(parent.context, R.drawable.item_divider)!!

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val vh = parent.getChildViewHolder(child)

            parent.getDecoratedBoundsWithMargins(child, mBounds)

            val left = mBounds.left
            val right = mBounds.right
            val bottom = mBounds.bottom
            val top = mBounds.top

            contour.setBounds(left, top, right, bottom)

            if (false) {
                contour.draw(canvas)
            }
        }
        canvas.restore()
    }

}