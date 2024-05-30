package com.alperen.cryptocurrencydemoapp.model.ticker

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
data class Ticker(
    var status: Boolean = false,
    var data: ArrayList<TickerData>? = null,
    var is_login: Boolean = false
)