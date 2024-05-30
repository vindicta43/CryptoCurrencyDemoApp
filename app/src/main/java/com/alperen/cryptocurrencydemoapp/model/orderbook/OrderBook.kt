package com.alperen.cryptocurrencydemoapp.model.orderbook

import com.google.gson.annotations.SerializedName

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
data class OrderBook(
    @SerializedName("buy")
    var buyOrders: List<BuyOrder>,

    @SerializedName("sell")
    var sellOrders: List<SellOrder>
)