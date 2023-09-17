package com.aitgacem.budgeter.ui.screens.home

import android.view.ViewGroup
import android.widget.LinearLayout
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aitgacem.budgeter.data.model.Transaction
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.aitgacem.budgeter.ui.viewmodels.utils.DateFormatter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.ramcosta.composedestinations.annotation.Destination

@HomeNavGraph
@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AnalyticsScreen(
    navController: NavController,
    analyticsViewModel: AnalyticsViewModel = hiltViewModel(),
) {
    val updateChart: (PieChart) -> Unit = { analyticsViewModel.updatePieChart(it) }
    val updateLineChart: (LineChart) -> Unit = { analyticsViewModel.updateLineChart(it) }

    val all = analyticsViewModel.allTransactions.collectAsState()
    Surface(color = MaterialTheme.colorScheme.primary) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Financial Analytics",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Left,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                )
            }
        ) { paddingValues ->


            AnalyticsScreenContent(
                modifier = Modifier.padding(paddingValues),
                updatePieChart = updateChart,
                updateLineChart = updateLineChart,
                updateEvent = all,
            )


        }
    }
}

@Composable
fun AnalyticsScreenContent(
    modifier: Modifier = Modifier,
    updatePieChart: (chart: PieChart) -> Unit,
    updateLineChart: (chart: LineChart) -> Unit,
    updateEvent: State<List<Transaction>>,
) {
    val update = updateEvent.value
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Text(
                text = "Spending by category",
                style = MaterialTheme.typography.bodyLarge
            )
        }
        item {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(18.dp)
                    .size(320.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                PieChart(
                    updateChart = updatePieChart,
                    updateEvent = updateEvent,
                )
            }
        }
        item {
            Column(
                modifier = androidx.compose.ui.Modifier
                    .padding(18.dp)
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LineChart(
                    updateChart = updateLineChart,
                    updateEvent = updateEvent,
                )
            }
        }
    }
}

@Composable
fun LineChart(
    updateChart: (chart: LineChart) -> Unit,
    updateEvent: State<List<Transaction>>,
) {
    val update = updateEvent.value
    println("heyy line")
    AndroidView(
        factory = { context ->
            println(context)
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
                setNoDataText("It is as empty as your life in here lol")
            }
        },
        modifier = Modifier
            .wrapContentSize()
            .padding(5.dp),
    ) {
        println("heyy updating line here")
        updateChart.invoke(it)
    }
}

@Composable
fun PieChart(
    updateChart: (chart: PieChart) -> Unit,
    updateEvent: State<List<Transaction>>,
) {
    val update = updateEvent.value
    println("heyy pie")

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
        println("heyy updating pie here")
        updateChart(it)
    }
}