package com.alperen.cryptocurrencydemoapp.model.ticker

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
data class Second(
    var id: Int = 0,
    var decimal: Int = 0,
    var iso3: String = "",
    var name: String = "",
    var rate_usd: Long = 0,
    var rate_usdt: Long = 0,
    var rate_btc: Int = 0,
    var rate_eth: Int = 0
)