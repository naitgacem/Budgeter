package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aitgacem.budgeter.databinding.FragmentAnalyticsScreenBinding
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.mapToList
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsScreenBinding
    private val viewModel: AnalyticsViewModel by viewModels()
    private val testDays: List<Pair<Int, Double>> = (1..30).map {
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
    private var chartData: List<Pair<Int, Double>> = mutableListOf()

    private val testCat = listOf(
        Pair(Category.Healthcare, 2400.0),
        Pair(Category.Entertainment, 500.0),
        Pair(Category.Education, 100.0),
        Pair(Category.Groceries, 1500.0),
        Pair(Category.Food, 600.0),
    )
    private var pieData: List<Pair<Category, Double>> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pieChart = binding.pieChart
        val pieChartModel: AAChartModel = AAChartModel()
            .chartType(AAChartType.Pie)
            .title("Spending by category")
            .dataLabelsEnabled(true)
            .series(
                arrayOf(
                    AASeriesElement()
                        .name("Total:")
                        .data(
                            testCat.map {
                                arrayOf(it.first.name, it.second)
                            }.toTypedArray()
                        )
                )
            ).dataLabelsEnabled(false)

        val lineChart = binding.lineChart



        viewModel.curMonthBalanceData.observe(viewLifecycleOwner) { map ->
            chartData = mapToList(map)
            lineChart.aa_drawChartWithChartModel(
                AAChartModel()
                    .chartType(AAChartType.Spline)
                    .title("Daily Balance")
                    .dataLabelsEnabled(true)
                    .categories(chartData.map {
                        it.first.toString() + "Jan"
                    }.toTypedArray())
                    .series(
                        arrayOf(
                            AASeriesElement()
                                .name("Balance: ")
                                .data(chartData.map {
                                    it.second
                                }.toTypedArray())
                        )
                    ).dataLabelsEnabled(false)
                    .legendEnabled(false)
                    .zoomType(AAChartZoomType.X)
            )
        }

        viewModel.curMonthSpendingData.observe(viewLifecycleOwner) { list ->
            pieData = list.filter {
                it.category != Category.Deposit
            }.map {
                Pair(it.category, it.value)
            }

            pieChart.aa_drawChartWithChartModel(
                AAChartModel()
                    .chartType(AAChartType.Pie)
                    .title("Spending by category")
                    .dataLabelsEnabled(true)
                    .series(
                        arrayOf(
                            AASeriesElement()
                                .name("Total:")
                                .data(
                                    pieData.map {
                                        arrayOf(it.first.name, it.second)
                                    }.toTypedArray()
                                )
                        )
                    )
                    .dataLabelsEnabled(false)
            )

        }

    }
}