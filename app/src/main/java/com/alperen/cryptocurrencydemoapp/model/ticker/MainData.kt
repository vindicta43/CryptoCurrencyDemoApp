package com.alperen.cryptocurrencydemoapp.model.ticker

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
data class MainData(
    var id: Int = 0,
    var decimal: Int = 0,
    var iso3: String = "",
    var name: String = "",
    var circulating_supply: String = "",
    var maximum_supply: String = "",
    var total_supply: String = "",
    var cap: Double = 0.0,
    var cap_f: String = "",
    var rate_usd: Long = 0,
    var rate_usdt: Long = 0,
    var rate_btc: Int = 0,
    var rate_eth: Int = 0
)