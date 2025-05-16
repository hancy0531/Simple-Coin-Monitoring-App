package com.example.coco.view.main

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.coco.R
import com.example.coco.databinding.FragmentCoinDetailBinding
import com.example.coco.repository.NetworkRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class CoinDetailFragment : Fragment() {

    private var coinName: String? = null

    private val networkRepository = NetworkRepository()

    private var _binding: FragmentCoinDetailBinding?= null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCoinDetailBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_coinDetailFragment_to_coinListFragment)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments.let { bundle: Bundle? ->
            coinName = bundle?.getString("coin")
            viewModel.getCoinData(coinName.toString())
            binding.coinName.text = coinName
            viewModel.coinData.observe(viewLifecycleOwner, Observer {
                binding.date.text = getDate(it.data["date"].toString()) + " 기준"
                binding.fluctateRate.text = it.data["fluctate_rate_24H"].toString()
                checkUpDown(it.data["fluctate_rate_24H"].toString())
                binding.openingPrice.text = it.data["opening_price"].toString()
                binding.closingPrice.text = it.data["closing_price"].toString()
                binding.minPrice.text = it.data["min_price"].toString()
                binding.maxPrice.text = it.data["max_price"].toString()
                binding.unitsTraded.text = it.data["units_traded"].toString()
                binding.prevClosingPrice.text = it.data["prev_closing_price"].toString()
            })
        }

    }

    fun checkUpDown(fluctateRate: String) {

        if (fluctateRate.contains("-")) {
            binding.coinName.setTextColor(Color.parseColor("#114fed"))
        } else {
            binding.coinName.setTextColor(Color.parseColor("#ed2e11"))
        }

    }

    fun getDate(timeStamp: String): String {

        val date = Date(timeStamp.toLong())
        val koreanFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        koreanFormat.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        val realDate = koreanFormat.format(date)

        return realDate

    }


}