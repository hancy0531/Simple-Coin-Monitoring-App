package com.example.coco.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coco.R
import com.example.coco.dataModel.CurrentPriceResult

class SelectRVAdapter(val context: Context, val coinPirceList: List<CurrentPriceResult>)
    : RecyclerView.Adapter<SelectRVAdapter.ViewHolder>(){

    val selectedCoinList = ArrayList<String>()

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val coinName: TextView = view.findViewById(R.id.coinName)
        val coinPriceUpDown: TextView = view.findViewById(R.id.coinPriceUpDown)
        val likeImage: ImageView = view.findViewById(R.id.likeBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.intro_coin_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return coinPirceList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.coinName.text = coinPirceList[position].coinName

        val fluctate_24H = coinPirceList[position].coinInfo.fluctate_24H
        if(fluctate_24H.contains("-")) {
            holder.coinPriceUpDown.text = "하락입니다."
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#114fed"))
        } else {
            holder.coinPriceUpDown.text = "상승입니다."
            holder.coinPriceUpDown.setTextColor(Color.parseColor("#ed2e11"))
        }

        val likeImage = holder.likeImage
        val currentCoin = coinPirceList[position].coinName

        if(selectedCoinList.contains(currentCoin)) {
            likeImage.setImageResource(R.drawable.like_red)
        } else {
            likeImage.setImageResource(R.drawable.like_grey)
        }

        likeImage.setOnClickListener {

            if(selectedCoinList.contains(currentCoin)) {
                selectedCoinList.remove(currentCoin)
                likeImage.setImageResource(R.drawable.like_grey)
            }else {
                selectedCoinList.add(currentCoin)
                likeImage.setImageResource(R.drawable.like_red)
            }
        }

    }
}