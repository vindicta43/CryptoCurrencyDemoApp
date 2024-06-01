package com.alperen.cryptocurrencydemoapp.ui

import android.graphics.Paint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.cryptocurrencydemoapp.R
import com.alperen.cryptocurrencydemoapp.databinding.FragmentChartBinding
import com.alperen.cryptocurrencydemoapp.model.Candlestick
import com.alperen.cryptocurrencydemoapp.model.NetworkResult
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderBook
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderType
import com.alperen.cryptocurrencydemoapp.model.ticker.Ticker
import com.alperen.cryptocurrencydemoapp.util.OrderBookRVAdapter
import com.alperen.cryptocurrencydemoapp.viewmodel.CryptoViewModel
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale


@AndroidEntryPoint
class ChartFragment : Fragment() {
    private lateinit var binding: FragmentChartBinding
    private val viewModel: CryptoViewModel by viewModels()
    private val handler = Handler()
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getViewModelData()
        observeViewModelData()
        initComponents()
    }

    private fun initComponents() {
        with(binding) {
            progress.visibility = View.VISIBLE
            btnBack.setOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun getViewModelData() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getBtcUsdtGraph()
            viewModel.getOrderBook()
            viewModel.getTickerData()
        }
    }

    private fun observeViewModelData() {
        runnable = Runnable {
            // Retrieving data for each 5 second
            // It is inefficient way to retrieve data but;
            // but more complex and stable structure coding would be coded in longer time
            getViewModelData()

            viewModel.btcUsdtLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        setCandlestickChart(response.data)
                        setVolumeChart(response.data)
                        binding.progress.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        Snackbar.make(
                            binding.root,
                            "An error occurred. Please try again",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.progress.visibility = View.GONE
                    }
                }
            }

            viewModel.orderBookLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {
                    }

                    is NetworkResult.Success -> {
                        setRecyclerViewData(response.data)
                        binding.progress.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        Snackbar.make(
                            binding.root,
                            "An error occurred. Please try again",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        binding.progress.visibility = View.GONE
                    }
                }
            }

            viewModel.tickerLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        setData(response.data)
                        binding.progress.visibility = View.GONE
                    }

                    is NetworkResult.Error -> {
                        Snackbar.make(
                            binding.root,
                            "An error occurred. Please try again",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            binding.rvBuyOrderBook.adapter?.notifyDataSetChanged()
            binding.rvSellOrderBook.adapter?.notifyDataSetChanged()
            handler.postDelayed(runnable, 5000)
        }
        handler.postDelayed(runnable, 0)
    }

    private fun setData(data: Ticker?) {
        binding.ticker = data

        data.let {
            // Percentage text change
            if (data?.data?.get(0)?.percent!! > 0) {
                binding.tvPercentage.setTextColor(resources.getColor(R.color.profit_green))
            } else {
                binding.tvPercentage.setTextColor(resources.getColor(R.color.loss_red))
            }

            // Price text change
            binding.tvCoinPrice.text = NumberFormat.getCurrencyInstance(Locale.US).format(
                (data.data?.get(0)?.main?.rate_usdt)?.toDouble()?.div(
                    100000000
                ) ?: 0
            )

            // Price equivilent change
            binding.tvCoinEquivilentPrice.text =
                NumberFormat.getCurrencyInstance(Locale.US).format(
                    (data.data?.get(0)?.main?.rate_usd)?.toDouble()?.div(
                        100000000
                    ) ?: 0
                )

            binding.tv24hHigh.text = data.data?.get(0)?.high_f
            binding.tv24hLow.text = data.data?.get(0)?.low_f
            binding.tv24hVolume.text = data.data?.get(0)?.volume_f + "M"
            binding.tv24hAmount.text =
                String.format("%.2f", data.data?.get(0)?.main?.circulating_supply?.toDouble())
        }
    }

    private fun setRecyclerViewData(data: OrderBook?) {
        data.let {
            binding.rvBuyOrderBook.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = OrderBookRVAdapter(data?.buyOrders ?: emptyList(), OrderType.BUY)
            }

            binding.rvSellOrderBook.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = OrderBookRVAdapter(data?.sellOrders ?: emptyList(), OrderType.SELL)
            }
        }
    }

    private fun setCandlestickChart(data: List<Candlestick>?) {
        with(binding) {
            val candleEntries = mutableListOf<CandleEntry>()
            data?.forEachIndexed { index, candlestick ->
                candleEntries.add(
                    candlestick.responseToCandlestickChartData(
                        index.toFloat()
                    )
                )
            }

            val candleDataset = CandleDataSet(candleEntries, null)
            candleDataset.apply {
                shadowColor = resources.getColor(R.color.white)
                decreasingColor = resources.getColor(R.color.profit_green)
                decreasingPaintStyle = Paint.Style.FILL
                increasingColor = resources.getColor(R.color.loss_red)
                increasingPaintStyle = Paint.Style.FILL
                setDrawValues(false)
                valueTextColor = resources.getColor(R.color.text_selected)
                formSize = 10f
            }

            val candleData = CandleData(candleDataset)
            candleStickChart.apply {
                description.text = ""
                this.data = candleData
                invalidate()
            }
        }
    }

    private fun setVolumeChart(data: List<Candlestick>?) {
        with(binding) {
            val barEntries = mutableListOf<BarEntry>()
            data?.forEachIndexed { index, candlestick ->
                barEntries.add(
                    candlestick.responseToBarChartData(
                        index.toFloat()
                    )
                )
            }

            val barDataset = BarDataSet(barEntries, null)
            barDataset.apply {
                setDrawValues(false)
                valueTextColor = resources.getColor(R.color.text_selected)
                formSize = 10f
            }

            val barData = BarData(barDataset)
            volumeChart.apply {
                description.text = ""
                this.data = barData
                invalidate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.tickerLiveData.removeObservers(viewLifecycleOwner)
        viewModel.orderBookLiveData.removeObservers(viewLifecycleOwner)
        handler.removeCallbacks(runnable)
    }
}