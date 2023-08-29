package com.aitgacem.budgeter.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.aitgacem.budgeter.data.model.CategoryAndValue
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavHostController,
    analyticsViewModel: AnalyticsViewModel = viewModel(factory = AnalyticsViewModel.Factory),
) {
    val categoryAndValues by analyticsViewModel.categoryAndValues.collectAsState(initial = null)
    val updateChart: (PieChart) -> Unit = { analyticsViewModel.updateChart(it) }

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
                updateChart = updateChart,
            )
        }
    }
}

@Composable
fun PieChart(
    modifier: Modifier = Modifier,
    categoryAndValues: List<CategoryAndValue>,
    updateChart: (chart: PieChart) -> Unit,
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
                updateChart(it)
            }
        }
    }
}

