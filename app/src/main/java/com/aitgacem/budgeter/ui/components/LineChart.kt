package com.aitgacem.budgeter.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.util.AttributeSet
import android.view.View

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

    private val mPaint = Paint()
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
        if (testDays.isEmpty()) {
            //Log.d("TAG", "onDraw: returning")
            return
        }
        canvas.drawColor(Color.GREEN)

        mPaint.apply {
            isAntiAlias = true
            color = Color.BLACK
            strokeWidth = 5f // Adjust the stroke width as needed
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
        }

        drawChart(canvas)
    }

    fun interpolate(start: Float, end: Float, fraction: Float): Float {
        return start + fraction * (end - start)
    }

    fun interpolatePoints(x1: Float, y1: Float, x2: Float, y2: Float, fraction: Float): PointF {
        val interpolatedX = interpolate(x1, x2, fraction)
        val interpolatedY = interpolate(y1, y2, fraction)
        return PointF(interpolatedX, interpolatedY)
    }

    private fun drawChart(canvas: Canvas) {
        val scaleX = usableWidth / testDays.size.toFloat()
        val scaleY = usableHeight / (minY - maxY).toFloat()
        val offset = -scaleY * maxY
        mPath.reset()
        val numberOfPoints = 50

        var prevX = paddingLeft + scaleX * testDays[0].first.toFloat()
        var prevY = paddingTop + scaleY * testDays[0].second.toFloat() + offset.toFloat()
        mPath.moveTo(prevX, prevY)
        var x = 0f
        var y = 0f

        var day: Pair<Int, Double>
        for (i in 1..<testDays.size) {
            day = testDays[i]
            x = paddingLeft + scaleX * day.first.toFloat()
            y = paddingTop + scaleY * day.second.toFloat() + offset.toFloat()
            //Log.d("TAG", "onDraw: x = ${x}, y = ${y}")
            canvas.drawPoint(x, y, mPaint)
            val interpolatedPoints = mutableListOf<PointF>()
            var tempx = prevX
            var tempy = prevY
            for (j in 0..numberOfPoints) {

                val fraction = i.toFloat() / numberOfPoints
                val interpolatedPoint: PointF = interpolatePoints(prevX, prevY, x, y, fraction)
                mPath.quadTo(tempx, tempy, interpolatedPoint.x, interpolatedPoint.y)
                tempx = interpolatedPoint.x
                tempy = interpolatedPoint.y

            }
            //canvas.drawLine(prevX, prevY, x, y, mPaint)
            //Log.d("TAG", "onDraw: prevx = ${prevX}, prevy = ${prevY}")
            prevX = x
            prevY = y
        }
        canvas.drawPath(mPath, mPaint)
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
        usableWidth = width - (paddingLeft + paddingRight)
        usableHeight = height - (paddingTop + paddingBottom)
        centerX = paddingLeft + usableWidth / 2
        centerY = paddingTop + usableHeight / 2
    }

}