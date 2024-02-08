package com.aitgacem.budgeter.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

import java.util.Calendar

class LineChart : View {
    /**
     * Points.x represents the day, in Unix timestamp
     *
     * Points.y represents the balance amount
     */
    private val mPoints = mutableListOf<PointF>()
    private val type: ViewType = ViewType.MONTH
    private val selectedMonth = Calendar.getInstance().get(Calendar.MONTH) //0 based
    private var maxY = 0.0
    private var minY = 0.0
    private val mDays = mutableListOf<Pair<Long, Double>>()
    private val mPaint = Paint()

    //programatically
    constructor(context: Context) : super(context) {

    }

    //through xml
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

    }

    fun init(days: List<Pair<Long, Double>>) {
        for (day in days) {
            if (day.second > maxY) {
                maxY = day.second
            }
            if (day.second < minY) {
                minY = day.second
            }
        }
        mDays.addAll(days)

    }

    private fun computePos() {
        val span = maxY - minY
        val scaleY = span.toFloat() / measuredHeight //px per DA
        val scaleX = width.toFloat() / 30 //day per px
        val zeroY = scaleY * maxY.toFloat()
        for (day in mDays) {
            mPoints.add(
                PointF(
                    getDaysSinceStartOfMonth(day.first) * scaleX,
                    zeroY - day.second.toFloat() * scaleY
                )
            )
        }
    }

    private fun getDaysSinceStartOfMonth(unixTimestamp: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = unixTimestamp

        // Set the calendar to the start of the month
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val startOfMonth = calendar.timeInMillis

        // Calculate the number of days since the start of the month
        val millisecondsSinceStartOfMonth = unixTimestamp - startOfMonth
        val daysSinceStartOfMonth = millisecondsSinceStartOfMonth / (24 * 60 * 60 * 1000)

        return daysSinceStartOfMonth
    }

    override fun onDraw(canvas: Canvas) {
        computePos()
        for (day in mPoints) {
            canvas.drawPoint(day.x, day.y, mPaint.apply {
                color = Color.BLACK
                strokeWidth = 15f // Adjust the stroke width as needed
                style = Paint.Style.STROKE
            })
        }
    }

    enum class ViewType {
        YEAR,
        MONTH,
        DAY,
    }
}