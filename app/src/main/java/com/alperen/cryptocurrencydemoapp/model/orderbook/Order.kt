package com.alperen.cryptocurrencydemoapp.model.orderbook

/**
 * Created by Alperen Erdoğan on 1.06.2024.
 */
data class Order(
    var volume: Int = 0,
    var count: Int = 0,
    var rate: Long = 0,
    var price: Int = 0,
    var rateF: String = "",
    var volumeF: String = ""
)

enum class OrderType {
    BUY,
    SELL
}