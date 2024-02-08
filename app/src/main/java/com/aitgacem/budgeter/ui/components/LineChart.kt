package com.aitgacem.budgeter.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.math.log

class LineChart : View {
    /**
     * Points.x represents the day, in Unix timestamp
     *
     * Points.y represents the balance amount
     */
    private val mPoints = mutableListOf<PointF>()
    private val type: ViewType = ViewType.MONTH

    private var maxY = 0.0
    private var minY = 0.0

    private val mPaint = Paint()
    private var width = 0
    private var height = 0
    private var paddingLeft = 0
    private var paddingRight = 0
    private var paddingTop = 0
    private var paddingBottom = 0
    private var centerX = 0
    private var centerY = 0

    private val testDays = (1..30).map {
        it to when (it) {
            1 -> 123.45
            2 -> -987.65
            3 -> 543.21
            4 -> -345.67
            5 -> 8765.43
            6 -> -1234.56
            7 -> 7890.12
            8 -> -9876.54
            9 -> 321.65
            10 -> -8765.43
            11 -> 4321.98
            12 -> -7654.32
            13 -> 1098.76
            14 -> -5432.10
            15 -> 9876.54
            16 -> -210.43
            17 -> 8765.43
            18 -> -5432.10
            19 -> 9876.54
            20 -> -210.43
            21 -> 8765.43
            22 -> -5432.10
            23 -> 9876.54
            24 -> -210.43
            25 -> 8765.43
            26 -> -5432.10
            27 -> 9876.54
            28 -> -210.43
            29 -> 8765.43
            30 -> -5432.10
            else -> 0.0
        }
    }


    //programatically
    constructor(context: Context) : super(context) {
        init()
    }

    //through xml
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    private fun init() {
        mPaint.apply {
            color = Color.BLACK
            strokeWidth = 15f // Adjust the stroke width as needed
            style = Paint.Style.FILL // Use FILL_AND_STROKE to fill the point
            isAntiAlias = true // Enable anti-aliasing for smoother rendering
        }
        var amount: Double
        for (day in testDays) {
            amount = day.second
            if (amount < minY) {
                minY = amount
            }
            if (amount > maxY) {
                maxY = amount
            }
        }
    }

    private fun computePos() {

    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.GREEN)
        val scaleX = width / testDays.size.toFloat()
        val scaleY = height / (minY - maxY).toFloat()
        val b = -scaleY * maxY
        mPaint.apply {
            color = Color.BLACK
            strokeWidth = 5f // Adjust the stroke width as needed
            style = Paint.Style.FILL // Use FILL_AND_STROKE to fill the point
            isAntiAlias = true // Enable anti-aliasing for smoother rendering
        }
        var prevX = 0f
        var prevY = 0f
        mPoints.clear()
        for (day in testDays) {
            val point =
                PointF(scaleX * day.first.toFloat(), scaleY * day.second.toFloat() + b.toFloat())
            mPoints.add(point)
            canvas.drawPoint(point.x, point.y, mPaint)
        }
        for (i in 0..<mPoints.size - 1) {
            val point = mPoints[i]
            val next = mPoints[i + 1]
            canvas.drawLine(point.x, point.y, next.x, next.y, mPaint)
        }

    }

    enum class ViewType {
        YEAR,
        MONTH,
        DAY,
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w
        height = h
        paddingLeft = getPaddingLeft()
        paddingRight = getPaddingRight()
        paddingTop = getPaddingTop()
        paddingBottom = getPaddingBottom()
        val usableWidth = width - (paddingLeft + paddingRight)
        val usableHeight = height - (paddingTop + paddingBottom)
        centerX = paddingLeft + usableWidth / 2
        centerY = paddingTop + usableHeight / 2
    }

}