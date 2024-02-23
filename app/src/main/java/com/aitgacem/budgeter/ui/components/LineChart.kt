package com.aitgacem.budgeter.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.graphics.withScale
import java.lang.Math.pow
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

class LineChart : View {
    /**
     * Points.x represents the day, in Unix timestamp
     *
     * Points.y represents the balance amount
     */
    private val mPoints = mutableListOf<PointF>()
    private val type: ViewType = ViewType.MONTH
    private val mPath = Path()
    private var maxY = 0.0
    private var minY = 0.0

    private val mPaintText = Paint()
    private val mPaintGrid = Paint()
    private val mPaintChart = Paint()
    private var width = 0
    private var height = 0
    private var paddingLeft = 0
    private var paddingRight = 0
    private var paddingTop = 0
    private var paddingBottom = 0
    private var centerX = 0
    private var centerY = 0
    private var usableWidth = 0
    private var usableHeight = 0
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
        mPaintChart.apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeWidth = 5f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }
        mPaintGrid.apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeWidth = .3f
            style = Paint.Style.STROKE
        }
        mPaintText.apply {
            isAntiAlias = true
            color = Color.BLACK
            style = Paint.Style.FILL_AND_STROKE
            textSize =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
        }
    }

    private fun computePos() {

    }


    override fun onDraw(canvas: Canvas) {
        if (testDays.isEmpty()) {
            //Log.d("TAG", "onDraw: returning")
            return
        }
        canvas.drawColor(Color.WHITE)


        drawGrid(canvas)
        drawChart(canvas)
    }

    private fun drawGrid(canvas: Canvas) {
        val order = floor(log10(maxY - minY)).toInt() - 1
        val steps = ceil((maxY - minY) / 10.0.pow(order)).toInt()
        val scaleY = usableHeight / steps.toFloat()
        val max = 10.0.pow(order + 1).toInt()

        for (i in 0..steps) {
            val y = paddingTop + i.toFloat() * scaleY
            val value = max - i * 10.0.pow(order).toInt()
            canvas.drawLine(paddingLeft.toFloat(), y, width.toFloat() - paddingRight, y, mPaintGrid)
            canvas.drawText(value.toString(), 0f, y, mPaintText)
        }
    }


    private fun drawChart(canvas: Canvas) {
        mPaintChart.apply {
            strokeWidth = 5f
        }
        val scaleX = usableWidth / testDays.size.toFloat()
        val scaleY = usableHeight / (minY - maxY).toFloat()
        val offset = -scaleY * maxY
        mPath.reset()

        var prevX = paddingLeft + scaleX * testDays[0].first.toFloat()
        var prevY = paddingTop + scaleY * testDays[0].second.toFloat() + offset.toFloat()
        mPath.moveTo(prevX, prevY)
        var x: Float
        var y: Float


        var day: Pair<Int, Double>
        for (i in 1..<testDays.size) {
            day = testDays[i]
            x = paddingLeft + scaleX * day.first.toFloat()
            y = paddingTop + scaleY * day.second.toFloat() + offset.toFloat()
            //Log.d("TAG", "onDraw: x = ${x}, y = ${y}")
            canvas.drawPoint(x, y, mPaintChart)
            mPath.lineTo(x, y)
            //canvas.drawLine(prevX, prevY, x, y, mPaint)
            //Log.d("TAG", "onDraw: prevx = ${prevX}, prevy = ${prevY}")
            prevX = x
            prevY = y
        }
        canvas.drawPath(mPath, mPaintChart)
    }

    enum class ViewType {
        YEAR, MONTH, DAY,
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        width = w
        height = h
        paddingLeft = getPaddingLeft()
        paddingRight = getPaddingRight()
        paddingTop = getPaddingTop()
        paddingBottom = getPaddingBottom()
        usableWidth = width - (paddingLeft + paddingRight)
        usableHeight = height - (paddingTop + paddingBottom)
        centerX = paddingLeft + usableWidth / 2
        centerY = paddingTop + usableHeight / 2
    }

}