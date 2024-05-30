package com.alperen.cryptocurrencydemoapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alperen.cryptocurrencydemoapp.databinding.FragmentTradeBinding
import com.alperen.cryptocurrencydemoapp.remote.RemoteApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TradeFragment : Fragment() {
    private lateinit var binding: FragmentTradeBinding

    @Inject
    lateinit var testInstance: RemoteApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTradeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initComponents()
    }

    private fun initComponents() {
        // TODO: Graph call test success
//        GlobalScope.launch {
//            val result = testInstance.getBtcUsdtGraph()
//
//            if (result.isSuccessful) {
//                result.body()?.forEach {
//                    Log.d("exzi_demo", "candlestick graph: ${it.time}, ${it.high}")
//                }
//            } else {
//                Log.d("exzi_demo", result.message())
//            }
//        }

        // TODO: Orderbook call test success
//        GlobalScope.launch {
//            val result = testInstance.getOrderBook()
//
//            if (result.isSuccessful) {
//                result.body()?.buyOrders?.forEach {
//                    Log.d("exzi_demo", "buy: ${it.price}, ${it.count}")
//                }
//
//                result.body()?.sellOrders?.forEach {
//                    Log.d("exzi_demo", "sell: ${it.price}, ${it.count}")
//                }
//            } else {
//                Log.d("exzi_demo", result.message())
//            }
//        }

        // TODO: Ticker call test - base url different
        GlobalScope.launch {
            val result = testInstance.getTicker()

            if (result.isSuccessful) {
                Log.d("exzi_demo", "islogin: ${result.body()?.is_login} ")
                Log.d("exzi_demo", "status: ${result.body()?.status} ")

                result.body()?.data?.forEach {
                    Log.d("exzi_demo", "ticker: ${it.name} ")
                }
            } else {
                Log.d("exzi_demo", result.message())
            }
        }
    }
}