package com.example.coco.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.coco.dataModel.UpDownDataSet
import com.example.coco.db.entity.InterestCoinEntity
import com.example.coco.network.model.CurrentPriceList
import com.example.coco.repository.DBRepository
import com.example.coco.repository.NetworkRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class MainViewModel: ViewModel() {

    private val dbRepository = DBRepository()
    private val networkRepository = NetworkRepository()
    lateinit var selectedCoinList: LiveData<List<InterestCoinEntity>>

    private val _arr15min = MutableLiveData<List<UpDownDataSet>>()
    val arr15min: LiveData<List<UpDownDataSet>>
        get() = _arr15min

    private val _arr30min = MutableLiveData<List<UpDownDataSet>>()
    val arr30min: LiveData<List<UpDownDataSet>>
        get() = _arr30min

    private val _arr45min = MutableLiveData<List<UpDownDataSet>>()
    val arr45min: LiveData<List<UpDownDataSet>>
        get() = _arr45min

    private val _coinData = MutableLiveData<CurrentPriceList>()
    val coinData: LiveData<CurrentPriceList>
        get() = _coinData

    //CoinListFragment

    fun getAllInterestCoinDatA() = viewModelScope.launch {
        val coinList = dbRepository.getAllInterestCoinData().asLiveData()
        selectedCoinList = coinList

    }

    fun updateInterestCoinData(interestCoinEntity: InterestCoinEntity) = viewModelScope.launch(Dispatchers.IO) {
        if (interestCoinEntity.selected) {
            interestCoinEntity.selected = false
        } else {
            interestCoinEntity.selected = true
        }

        dbRepository.updateInterestCoinData(interestCoinEntity)
    }

    //PriceChangeFragment

    fun getAllSelectedCoinData() = viewModelScope.launch(Dispatchers.IO) {

        val selectedCoinList = dbRepository.getAllInterestSelectedCoinData()

        val arr15min = ArrayList<UpDownDataSet>()
        val arr30min = ArrayList<UpDownDataSet>()
        val arr45min = ArrayList<UpDownDataSet>()

        for (data in selectedCoinList) {
            val coinName = data.coin_name
            val oneCoinData = dbRepository.getOneSelectedCoinData(coinName).reversed()

            val size = oneCoinData.size

            if (size > 1) {
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[1].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr15min.add(upDownDataSet)
            }

            if (size > 2) {
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[2].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr30min.add(upDownDataSet)
            }

            if (size > 3) {
                val changedPrice = oneCoinData[0].price.toDouble() - oneCoinData[3].price.toDouble()
                val upDownDataSet = UpDownDataSet(
                    coinName,
                    changedPrice.toString()
                )
                arr45min.add(upDownDataSet)
            }
        }

        withContext(Dispatchers.Main) {
            _arr15min.value = arr15min
            _arr30min.value = arr30min
            _arr45min.value = arr45min
        }



    }

    // CoinDetailFragment

    fun getCoinData(coin: String) = viewModelScope.launch {
        //Timber.d(coin)
        _coinData.value =  networkRepository.getCoinData(coin)
        Timber.d(coinData.value?.data.toString())
    }



}