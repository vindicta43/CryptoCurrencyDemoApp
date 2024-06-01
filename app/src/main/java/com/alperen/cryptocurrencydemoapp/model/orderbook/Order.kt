package com.alperen.cryptocurrencydemoapp.model.orderbook

import com.google.gson.annotations.SerializedName

/**
 * Created by Alperen Erdoğan on 1.06.2024.
 */
data class Order(
    var volume: Int = 0,
    var count: Int = 0,
    var rate: Long = 0,
    var price: Int = 0,
    @SerializedName("rate_f") var rateF: String = "",
    @SerializedName("volume_f") var volumeF: String = ""
)

enum class OrderType {
    BUY,
    SELL
}