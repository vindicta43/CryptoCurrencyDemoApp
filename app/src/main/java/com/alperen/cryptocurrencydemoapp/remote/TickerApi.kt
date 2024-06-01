package com.alperen.cryptocurrencydemoapp.remote

import com.alperen.cryptocurrencydemoapp.model.ticker.Ticker
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Alperen Erdoğan on 1.06.2024.
 */
interface TickerApi {

    @GET("api/default/ticker")
    suspend fun getTicker(): Response<Ticker>
}