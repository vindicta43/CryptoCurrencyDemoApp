package com.alperen.cryptocurrencydemoapp.di

import com.alperen.cryptocurrencydemoapp.remote.RemoteApi
import com.alperen.cryptocurrencydemoapp.remote.TickerApi
import com.alperen.cryptocurrencydemoapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Alperen Erdoğan on 29.05.2024.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRemoteApi(): RemoteApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RemoteApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTickerApi(): TickerApi {
        return Retrofit.Builder()
            .baseUrl(Constants.TICKER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TickerApi::class.java)
    }
}