package com.alperen.cryptocurrencydemoapp.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.alperen.cryptocurrencydemoapp.databinding.LayoutRvItemBuyBinding
import com.alperen.cryptocurrencydemoapp.databinding.LayoutRvItemSellBinding
import com.alperen.cryptocurrencydemoapp.model.orderbook.Order
import com.alperen.cryptocurrencydemoapp.model.orderbook.OrderType

/**
 * Created by Alperen Erdoğan on 1.06.2024.
 */
class OrderBookRVAdapter(private val orderBookList: List<Order>, val orderType: OrderType) :
    RecyclerView.Adapter<OrderBookRVAdapter.OrderBookViewHolder>() {
    inner class OrderBookViewHolder(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            when (orderType) {
                OrderType.BUY -> {
                    with(binding as LayoutRvItemBuyBinding) {
                        tvPrice.text = order.price.toString()
                        tvQuantity.text = order.count.toString()
                    }
                }

                else -> {
                    with(binding as LayoutRvItemSellBinding) {
                        tvPrice.text = order.price.toString()
                        tvQuantity.text = order.count.toString()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookViewHolder {
        return when (viewType) {
            OrderType.BUY.ordinal -> {
                OrderBookViewHolder(
                    LayoutRvItemBuyBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> {
                OrderBookViewHolder(
                    LayoutRvItemSellBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int = orderBookList.size

    override fun onBindViewHolder(holder: OrderBookViewHolder, position: Int) {
        holder.bind(orderBookList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return orderType.ordinal
    }
}