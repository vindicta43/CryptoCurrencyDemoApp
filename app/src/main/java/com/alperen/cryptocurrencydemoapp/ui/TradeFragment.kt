package com.alperen.cryptocurrencydemoapp.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alperen.cryptocurrencydemoapp.R
import com.alperen.cryptocurrencydemoapp.databinding.FragmentTradeBinding
import com.alperen.cryptocurrencydemoapp.model.NetworkResult
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderBook
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderType
import com.alperen.cryptocurrencydemoapp.model.ticker.Ticker
import com.alperen.cryptocurrencydemoapp.remote.RemoteApi
import com.alperen.cryptocurrencydemoapp.remote.TickerApi
import com.alperen.cryptocurrencydemoapp.util.OrderBookRVAdapter
import com.alperen.cryptocurrencydemoapp.viewmodel.CryptoViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class TradeFragment : Fragment() {
    private lateinit var binding: FragmentTradeBinding
    private val viewModel: CryptoViewModel by viewModels()
    val handler = Handler()
    private lateinit var runnable: Runnable

    @Inject
    lateinit var remoteApi: RemoteApi

    @Inject
    lateinit var tickerApi: TickerApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTradeBinding.inflate(inflater)
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
            ivCandlestick.setOnClickListener { navigateToChartFragment() }
            rbBuy.isChecked = true
        }
    }

    private fun getViewModelData() {
        CoroutineScope(Dispatchers.Main).launch {
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
                        binding.progress.visibility = View.GONE
                    }
                }
            }

            viewModel.orderBookLiveData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkResult.Loading -> {

                    }

                    is NetworkResult.Success -> {
                        setRecyclerViews(response.data)
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

            binding.rvBuyOrderBook.adapter?.notifyDataSetChanged()
            binding.rvSellOrderBook.adapter?.notifyDataSetChanged()
            handler.postDelayed(runnable, 5000)
        }
        handler.postDelayed(runnable, 0)
    }

    private fun setRecyclerViews(data: OrderBook?) {
        binding.rvSellOrderBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.rvSellOrderBook.adapter =
            OrderBookRVAdapter(data?.sellOrders ?: emptyList(), OrderType.SELL)

        binding.rvBuyOrderBook.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBuyOrderBook.adapter =
            OrderBookRVAdapter(data?.buyOrders ?: emptyList(), OrderType.BUY)
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
            binding.tvOrderBookPrice.text = NumberFormat.getCurrencyInstance(Locale.US).format(
                (data.data?.get(0)?.main?.rate_usdt)?.toDouble()?.div(
                    100000000
                ) ?: 0
            )

            // Price equivilent change
            binding.tvOrderBookEquivilentPrice.text =
                NumberFormat.getCurrencyInstance(Locale.US).format(
                    (data.data?.get(0)?.main?.rate_usd)?.toDouble()?.div(
                        100000000
                    ) ?: 0
                )
        }
    }

    private fun navigateToChartFragment() {
        findNavController().navigate(R.id.action_tradeFragment_to_chartFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.tickerLiveData.removeObservers(viewLifecycleOwner)
        viewModel.orderBookLiveData.removeObservers(viewLifecycleOwner)
        handler.removeCallbacks(runnable)
    }
}