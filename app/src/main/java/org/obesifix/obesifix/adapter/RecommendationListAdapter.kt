package org.obesifix.obesifix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.obesifix.obesifix.databinding.ItemColHomeRecommendationBinding

class RecommendationListAdapter:
    PagingDataAdapter<,RecommendationListAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemColHomeRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class MyViewHolder(private val binding: ItemColHomeRecommendationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ) {
            binding.tvItemQuote.text = data.en
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecommendationResponseItem>() {
            override fun areItemsTheSame(oldItem: RecommendationResponseItem, newItem: RecommendationResponseItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: RecommendationResponseItem, newItem: RecommendationResponseItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}