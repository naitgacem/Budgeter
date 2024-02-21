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
    private var chartData: List<Pair<Int, Double>> = mutableListOf()

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
                    .yAxisTitle("")
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