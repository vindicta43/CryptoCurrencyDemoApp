package com.alperen.cryptocurrencydemoapp.remote

import com.alperen.cryptocurrencydemoapp.model.Candlestick
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderBook
import com.alperen.cryptocurrencydemoapp.model.ticker.Ticker
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Alperen Erdoğan on 30.05.2024.
 */
interface RemoteApi {

    @GET("graph/hist?t=BTCUSDT&r=D&limit=5000&end=1705363200")
    suspend fun getBtcUsdtGraph(): Response<List<Candlestick>>

    @GET("book/list?pair_id=1041&buy=1&sell=1")
    suspend fun getOrderBook(): Response<OrderBook>

    @GET("api/default/ticker")
    suspend fun getTicker(): Response<Ticker>
}