package com.example.coco.repository

import com.example.coco.network.Api
import com.example.coco.network.RetrofitInstance

class NetworkRepository {

    private val client = RetrofitInstance.getInstance().create(Api::class.java)

    suspend fun getCurrentCoinList() = client.getCurrentCoinList()

    suspend fun getInterestCoinPriceData(coin: String) = client.getRecentCoinPrice(coin)

    suspend fun getCoinData(coin: String) = client.getCoinDetail(coin)
}