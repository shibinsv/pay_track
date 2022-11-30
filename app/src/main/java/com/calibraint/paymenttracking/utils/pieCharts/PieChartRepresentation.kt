package com.calibraint.paymenttracking.utils.pieCharts

import android.content.Context
import android.graphics.Typeface
import com.calibraint.paymenttracking.R
import com.calibraint.paymenttracking.room.HouseMember
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF

object PieChartRepresentation {

    /**
     * @author Shibin
     * This function is used to display pieChart
     */
    fun initializePieChart(context: Context, pieChart: PieChart, pieInformation: PieChartData) {
        pieChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            dragDecelerationFrictionCoef = 0.95f

            // on below line we are setting hole
            // and hole color for pie chart
            isDrawHoleEnabled = true
            setHoleColor(R.color.white)

            // on below line we are setting circle color and alpha
            setTransparentCircleColor(R.color.white)
            setTransparentCircleAlpha(110)

            // on  below line we are setting hole radius
            holeRadius = 58f
            transparentCircleRadius = 61f

            // on below line we are setting center text
            setDrawCenterText(true)
            setCenterTextColor(R.color.red)
            centerText = pieInformation.pieCentreText

            // on below line we are setting
            // rotation for our pie chart
            rotationAngle = 0f

            // enable rotation of the pieChart by touch
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            // on below line we are setting animation for our pie chart
            animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            legend.isEnabled = false
            setEntryLabelColor(R.color.white)
            setEntryLabelTextSize(12f)

            // on below line we are setting pie data set
            val dataSet = PieDataSet(pieInformation.pieEntries, pieInformation.pieCentreText)

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

//            // add a lot of colors to list
            val colors: ArrayList<Int> = ArrayList()
            colors.add(context.getColor(R.color.primaryColor))
            colors.add(context.getColor(R.color.grey))
            dataSet.colors = colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            setData(data)


            // undo all highlights
            highlightValues(null)

            // loading chart
            invalidate()
        }

    }

    /**
     * @author Shibin
     * This function is used to get the entries for pie representation
     */
    fun getPieEntries(data: List<HouseMember>): ArrayList<PieEntry> {
        val entries: ArrayList<PieEntry> = arrayListOf()
        val totalSize = data.size
        val paidSize = data.filter { it.isPaid }.size.toFloat()
        val unPaidSize = data.filter { !it.isPaid }.size.toFloat()
        val paidPercent = ((paidSize / totalSize) * 100)
        val unPaidPercent = (unPaidSize / totalSize) * 100
        entries.add(PieEntry(paidPercent, "Paid"))
        entries.add(PieEntry(unPaidPercent, "Unpaid"))
//        if (paidPercent > 0) entries.add(PieEntry(paidPercent))
//        if (unPaidPercent > 0) entries.add(PieEntry(unPaidPercent))
        return entries
    }
}