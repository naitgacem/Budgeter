package com.aitgacem.budgeter.ui.fragments

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.aitgacem.budgeter.R
import com.aitgacem.budgeter.databinding.FragmentAnalyticsScreenBinding
import com.aitgacem.budgeter.ui.components.Category
import com.aitgacem.budgeter.ui.mapToList
import com.aitgacem.budgeter.ui.mapYear
import com.aitgacem.budgeter.ui.toMonthStr
import com.aitgacem.budgeter.ui.viewmodels.AnalyticsViewModel
import com.aitgacem.budgeter.ui.viewmodels.ChartViewType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartFontWeightType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartView
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartZoomType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.github.aachartmodel.aainfographics.aachartcreator.aa_toAAOptions
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AADataLabels
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAInactive
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAItemStyle
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AALegend
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AAStates
import com.github.aachartmodel.aainfographics.aaoptionsmodel.AATooltip
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnalyticsFragment : Fragment() {
    private lateinit var binding: FragmentAnalyticsScreenBinding
    private val viewModel: AnalyticsViewModel by viewModels()
    private var chartData: List<Pair<Int, Double>> = mutableListOf()
    private var pieData: List<Pair<Category, Double>> = mutableListOf()
    private var viewType = ChartViewType.MONTH_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsScreenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTransitions()
        viewModel.chartViewType.observe(viewLifecycleOwner) {
            viewType = it
            if (it == ChartViewType.YEAR_VIEW) {
                binding.monthDsp.visibility = GONE
            } else {
                binding.monthDsp.visibility = VISIBLE
            }
        }

        val pieChart = binding.pieChart
        val lineChart = binding.lineChart


        setupViewChanger()
        setupLineChart(lineChart)
        setupPieChart(pieChart)


        viewModel.curMonth.observe(viewLifecycleOwner) {
            binding.monthDsp.text = it.toMonthStr()
        }

        viewModel.curYear.observe(viewLifecycleOwner) {
            binding.yearDsp.text = it.toString()
        }

        binding.nextBtn.setOnClickListener { viewModel.moveForward() }
        binding.prevButton.setOnClickListener { viewModel.moveBackward() }
    }

    private fun setupViewChanger() {
        binding.viewType.check(R.id.month_btn)
        binding.viewType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked && checkedId == R.id.month_btn) {
                viewModel.switchViewType(ChartViewType.MONTH_VIEW)
            } else if (isChecked && checkedId == R.id.year_btn) {
                viewModel.switchViewType(ChartViewType.YEAR_VIEW)
            }
        }
    }


    private fun setupPieChart(pieChart: AAChartView) {

        val typedValue = TypedValue()
        requireActivity().theme.resolveAttribute(android.R.attr.colorForeground, typedValue, true)
        val foregroundColor =
            "#" + TypedValue.coerceToString(typedValue.type, typedValue.data).substring(3)



        viewModel.curMonthSpendingData.observe(viewLifecycleOwner) { list ->
            pieData = list.filter {
                it.category != Category.Deposit
            }.map {
                Pair(it.category, it.value)
            }
            if (pieData.isEmpty()) {
                binding.noDataPie.visibility = VISIBLE
                binding.noDataPie.bringToFront()
            } else {
                binding.noDataPie.visibility = GONE
            }

            pieChart.aa_drawChartWithChartOptions(
                AAChartModel().chartType(AAChartType.Pie).series(
                    arrayOf(
                        AASeriesElement().name(getString(R.string.total)).data(
                            pieData.map {
                                arrayOf(it.first.name, it.second)
                            }.toTypedArray()

                        ).allowPointSelect(false)
                            .states(AAStates().inactive(AAInactive().enabled(false)))
                            .tooltip(AATooltip().followTouchMove(true))
                            .dataLabels(AADataLabels().enabled(false))
                    )
                ).tooltipEnabled(true).backgroundColor("").animationDuration(0).aa_toAAOptions()
                    .legend(
                        AALegend().itemStyle(
                            AAItemStyle().color(foregroundColor)
                                .fontWeight(AAChartFontWeightType.Thin)
                        )
                    )

            )

        }
    }

    private fun setupLineChart(lineChart: AAChartView) {
        viewModel.curMonthBalanceData.observe(viewLifecycleOwner) { map ->
            chartData = if (viewType == ChartViewType.MONTH_VIEW) mapToList(map) else mapYear(map)
            if (chartData.isEmpty()) {
                binding.noDataLine.visibility = VISIBLE
                binding.noDataLine.bringToFront()
            } else {
                binding.noDataLine.visibility = GONE
            }

            lineChart.aa_drawChartWithChartModel(
                AAChartModel().chartType(AAChartType.Spline).dataLabelsEnabled(true)
                    .categories(chartData.map {
                        if (viewType == ChartViewType.MONTH_VIEW) "${it.first} ${
                            viewModel.curMonth.value.toMonthStr(true)
                        }"
                        else it.first.toMonthStr(true)
                    }.toTypedArray()).series(
                        arrayOf(
                            AASeriesElement().name(getString(R.string.balance)).data(chartData.map {
                                it.second
                            }.toTypedArray())
                        )
                    ).dataLabelsEnabled(false).legendEnabled(false).zoomType(AAChartZoomType.X)
                    .yAxisTitle("").tooltipEnabled(true).backgroundColor("").animationDuration(0)
            )

        }
    }

    private fun setupTransitions() {
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = 200
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = 200
        }
    }
}