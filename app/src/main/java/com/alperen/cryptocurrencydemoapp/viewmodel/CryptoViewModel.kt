package com.alperen.cryptocurrencydemoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alperen.cryptocurrencydemoapp.model.Candlestick
import com.alperen.cryptocurrencydemoapp.model.NetworkResult
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderBook
import com.alperen.cryptocurrencydemoapp.model.ticker.Ticker
import com.alperen.cryptocurrencydemoapp.remote.RemoteApi
import com.alperen.cryptocurrencydemoapp.remote.TickerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Alperen Erdoğan on 1.06.2024.
 */
@HiltViewModel
class CryptoViewModel @Inject constructor(private val remoteApi: RemoteApi, private val tickerApi: TickerApi) : ViewModel() {

    private var _btcUsdtLiveData: MutableLiveData<NetworkResult<List<Candlestick>>> =
        MutableLiveData<NetworkResult<List<Candlestick>>>()
    val btcUsdtLiveData: LiveData<NetworkResult<List<Candlestick>>> = _btcUsdtLiveData

    private var _orderBookLiveData: MutableLiveData<NetworkResult<OrderBook>> =
        MutableLiveData<NetworkResult<OrderBook>>()
    val orderBookLiveData: LiveData<NetworkResult<OrderBook>> = _orderBookLiveData

    private var _tickerLiveData: MutableLiveData<NetworkResult<Ticker>> =
        MutableLiveData<NetworkResult<Ticker>>()
    val tickerLiveData: LiveData<NetworkResult<Ticker>> = _tickerLiveData

    suspend fun getOrderBook() {
        viewModelScope.launch {
            _orderBookLiveData.value = NetworkResult.Loading()
            val apiResult = remoteApi.getOrderBook()

            if (apiResult.isSuccessful) {
                _orderBookLiveData.value = NetworkResult.Success(apiResult.body()!!)
            } else {
                _orderBookLiveData.value = NetworkResult.Error(apiResult.errorBody().toString())
            }
        }
    }

    suspend fun getBtcUsdtGraph() {
        viewModelScope.launch {
            _btcUsdtLiveData.value = NetworkResult.Loading()
            val apiResult = remoteApi.getBtcUsdtGraph()

            if (apiResult.isSuccessful) {
                _btcUsdtLiveData.value = NetworkResult.Success(apiResult.body()!!)
            } else {
                _btcUsdtLiveData.value = NetworkResult.Error(apiResult.errorBody().toString())
            }
        }
    }

    suspend fun getTickerData() {
        viewModelScope.launch {
            _tickerLiveData.value = NetworkResult.Loading()
            val apiResult = tickerApi.getTicker()

            if (apiResult.isSuccessful) {
                _tickerLiveData.value = NetworkResult.Success(apiResult.body()!!)
            } else {
                _tickerLiveData.value = NetworkResult.Error(apiResult.errorBody().toString())
            }
        }
    }
}