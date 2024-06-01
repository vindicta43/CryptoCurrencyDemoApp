package com.alperen.cryptocurrencydemoapp.model

import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleEntry
import com.google.gson.annotations.SerializedName

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
data class Candlestick(
    var low: Long = 0,
    var high: Long = 0,
    var volume: Long = 0,
    var time: Int = 0,
    var open: Long = 0,
    var close: Long = 0,
    @SerializedName("pair_id") var pairId: Int = 0,
    @SerializedName("low_f") var lowF: String = "",
    @SerializedName("high_f") var highF: String = "",
    @SerializedName("open_f") var openF: String = "",
    @SerializedName("close_f") var closeF: String = "",
    @SerializedName("volume_f") var volumeF: String = ""
) {
    fun responseToCandlestickChartData(index: Float): CandleEntry {
        return CandleEntry(
            index,
            this.highF.toFloat(),
            this.lowF.toFloat(),
            this.openF.toFloat(),
            this.closeF.toFloat(),
        )
    }

    fun responseToBarChartData(index: Float): BarEntry {
        return BarEntry(
            this.volumeF.toFloat(),
            index
        )
    }
}