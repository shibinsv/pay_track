package com.calibraint.paymenttracking.utils.pieCharts

import com.github.mikephil.charting.data.PieEntry

data class PieChartData(
    var pieCentreText: String = "", //text present at the centre of the pie
    var pieEntries: ArrayList<PieEntry> = arrayListOf()// specifies the float entries to be displayed in the piechart
)
