package com.aitgacem.budgeter.ui.screens

import android.graphics.Typeface
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavHostController,
    analyticsViewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory),
) {
    val categoryAndValues by analyticsViewModel.categoryAndValues.collectAsState(initial = null)

    Surface(color = MaterialTheme.colorScheme.primary) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Pie Chart Example",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    })
            }) { paddingValues ->
            PieChart(
                modifier = Modifier.padding(paddingValues),
                categoryAndValues = categoryAndValues ?: emptyList(),
            )
        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    categoryAndValues: List<CategoryAndValue>,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Spending by category chart",
        )
        Column(
            modifier = Modifier
                .padding(18.dp)
                .size(320.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AndroidView(
                factory = { context ->
                    PieChart(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        this.description.isEnabled = false
                        this.isDrawHoleEnabled = false
                        this.legend.isEnabled = true
                        this.legend.textSize = 14F
                        this.legend.horizontalAlignment =
                            Legend.LegendHorizontalAlignment.CENTER
                        this.setEntryLabelColor(0)
                    }
                },
                modifier = Modifier
                    .wrapContentSize()
                    .padding(5.dp)
            ) {
                updatePieChartWithData(it, categoryAndValues)
            }
        }
    }
}

fun updatePieChartWithData(
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

    ds.colors = arrayListOf(
        greenColor.toArgb(),
        blueColor.toArgb(),
        redColor.toArgb(),
        yellowColor.toArgb(),
        purpleColor.toArgb(),
        purple2Color.toArgb(),
        greyColor.toArgb(),
        green2Color.toArgb(),
    )
    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE
    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.sliceSpace = 2f
    ds.valueTextColor = Color(0xFF000000).toArgb()
    ds.valueTextSize = 18f
    ds.valueTypeface = Typeface.DEFAULT

    chart.data = PieData(ds)
    chart.invalidate()
}

data class CategoryAndValue(
    var category: Category,
    var value: Float,
)


val greenColor = Color(0xFF0F9D58)
val blueColor = Color(0xFF2196F3)
val yellowColor = Color(0xFFFFC107)
val redColor = Color(0xFFFF0000)
val purpleColor = Color(0xFF9C27B0)
val green2Color = Color(0xFFCDDC39)
val greyColor = Color(0xFF998989)
val purple2Color = Color(0xFFE600FF)


