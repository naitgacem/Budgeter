package com.aitgacem.budgeter.ui.screens.home

import android.graphics.Typeface
import android.os.Build
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.data.model.DateAndBalance
import com.aitgacem.budgeter.databinding.FragmentAnalyticsScreenBinding
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.aitgacem.budgeter.ui.viewmodels.utils.DateFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalyticsScreen(
    analyticsViewModel: AnalyticsViewModel = hiltViewModel(),
) {
    val categoryAndValues by analyticsViewModel.categoryAndValues.collectAsState()
    val dateAndBalance by analyticsViewModel.dateAndBalance.collectAsState()

    val updateChart: (PieChart) -> Unit = { updatePieChartWithData(it, categoryAndValues) }
    val updateLineChart: (LineChart) -> Unit = { updateLineChartWithData(it, dateAndBalance) }
    val textColor = Color.White
    Surface(color = MaterialTheme.colorScheme.primary) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    text = "Financial Analytics",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    style = MaterialTheme.typography.titleLarge
                )
            })
        }) { paddingValues ->

            AndroidViewBinding(
                modifier = Modifier.padding(paddingValues),
                factory = FragmentAnalyticsScreenBinding::inflate
            ) {
                this.lineChart.apply {
                    isLogEnabled = false
                    axisLeft.setDrawGridLines(false)
                    axisRight.setDrawGridLines(false)
                    xAxis.setDrawGridLines(false)
                    description.isEnabled = false
                    legend.isEnabled = false
                    xAxis.valueFormatter = DateFormatter()
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.textColor = Color.White.toArgb()
                    axisLeft.textColor = Color.White.toArgb()
                    axisRight.isEnabled = false

                }
                this.pieChart.apply {
                    description.isEnabled = false
                    isDrawHoleEnabled = false
                    legend.isEnabled = true
                    legend.textSize = 14F
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                    legend.isWordWrapEnabled = true
                    isLogEnabled = true
                    setEntryLabelColor(0)
                    setUsePercentValues(true)
                    description.textColor = textColor.toArgb()


                }
                updateLineChart(this.lineChart)
                updateChart(this.pieChart)
            }
        }
    }
}

@Composable
fun AnalyticsScreenContent(
    modifier: Modifier = Modifier,
    updatePieChart: (chart: PieChart) -> Unit,
    updateLineChart: (chart: LineChart) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                text = "Spending by category", style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .size(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PieChart(
                    updateChart = updatePieChart,
                )
            }
        }
        item {
            Column(
                modifier = Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LineChart(
                    updateChart = updateLineChart,
                )
            }
        }
    }
}

@Composable
fun LineChart(
    updateChart: (chart: LineChart) -> Unit,
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                isLogEnabled = false
                axisLeft.setDrawGridLines(false)
                axisRight.setDrawGridLines(false)
                xAxis.setDrawGridLines(false)
                description.isEnabled = false
                legend.isEnabled = false
                xAxis.valueFormatter = DateFormatter()
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                axisRight.isEnabled = false
            }
        },
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
    ) {
        updateChart.invoke(it)
    }
}

@Composable
fun PieChart(
    updateChart: (chart: PieChart) -> Unit,
) {
    AndroidView(
        factory = { context ->
            PieChart(context).apply {
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                description.isEnabled = false
                isDrawHoleEnabled = false
                legend.isEnabled = true
                legend.textSize = 14F
                legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
                legend.isWordWrapEnabled = true
                isLogEnabled = true
                setEntryLabelColor(0)
                setUsePercentValues(true)

            }
        },
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
    ) {
        updateChart(it)
    }
}

private fun updatePieChartWithData(
    chart: PieChart,
    data: List<CategoryAndValue>,
) {
    val entries = ArrayList<PieEntry>()
    for (item in data) {
        entries.add(
            PieEntry(item.value, item.category.name)
        )
    }

    val ds = PieDataSet(entries, "")
    setPieChartProperties(ds)
    chart.extraBottomOffset = 10f;
    chart.extraLeftOffset = 30f;
    chart.extraRightOffset = 30f;
    ds.valueFormatter = PercentFormatter(chart)
    ds.valueTextColor = Color.White.toArgb()
    ds.valueLineColor = Color.White.toArgb()
    chart.data = PieData(ds)
    chart.invalidate()
}

private fun setPieChartProperties(ds: PieDataSet) {
    ds.apply {
        yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        valueLinePart1OffsetPercentage = 100f
        valueLinePart1Length = 0.6f
        valueLinePart2Length = 0.4f
        sliceSpace = 2f
        valueTextColor = Color(0xFF000000).toArgb()
        valueTextSize = 18f
        valueTypeface = Typeface.DEFAULT
        colors = com.aitgacem.budgeter.ui.components.colors.map {
            it.toArgb()
        }
    }
}

private fun updateLineChartWithData(
    chart: LineChart,
    data: List<DateAndBalance>,
) {
    val entries = ArrayList<Entry>()
    for (item in data) {
        entries.add(
            Entry(item.date.toFloat(), item.amount)
        )
    }

    val ds = LineDataSet(entries, "")
    setLineChartProperties(ds)
    ds.color = Color.White.toArgb()
    chart.data = LineData(ds)
    chart.fitScreen()
    chart.setVisibleXRange(0f, (7 * 24 * 3600 * 1000).toFloat()) // between 0 and 10 days
    chart.moveViewToX(chart.xChartMax)
}

private fun setLineChartProperties(ds: LineDataSet) {
    ds.apply {
        valueTextColor = Color(0xFF000000).toArgb()
        valueTextSize = 18f
        valueTypeface = Typeface.DEFAULT
        setDrawValues(false)
        circleColors = listOf(Color.Blue.toArgb())
    }
}