package com.alperen.cryptocurrencydemoapp.model

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
    var pairId: Int = 0,
    var lowF: String = "",
    var highF: String = "",
    var openF: String = "",
    var closeF: String = "",
    var volumeF: String = ""
)