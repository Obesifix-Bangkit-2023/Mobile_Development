package org.obesifix.obesifix.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.obesifix.obesifix.databinding.ItemColHomeRecommendationBinding
import org.obesifix.obesifix.network.response.FoodListItem
import org.obesifix.obesifix.ui.detail.DetailActivity

class RecommendationListAdapter:
    PagingDataAdapter<FoodListItem,RecommendationListAdapter.MyViewHolder>(DIFF_CALLBACK){

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
        fun bind(data: FoodListItem) {
            Glide.with(binding.root.context)
                .load(data.image)
                .into(binding.imgCal)
            with(binding){
                tvTitleCal.text = data.name
                tvCalDesc.text = data.calorie.toString()
            }
            itemView.setOnClickListener {
                val intentDetail = Intent(itemView.context, DetailActivity::class.java)
                intentDetail.putExtra(DetailActivity.EXTRA_ID, data)
                itemView.context.startActivity(intentDetail)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FoodListItem>() {
            override fun areItemsTheSame(oldItem: FoodListItem, newItem: FoodListItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FoodListItem, newItem: FoodListItem): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }
}