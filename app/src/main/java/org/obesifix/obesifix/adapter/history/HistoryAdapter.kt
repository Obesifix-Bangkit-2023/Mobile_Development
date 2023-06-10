package org.obesifix.obesifix.adapter.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.databinding.ItemHistoryBinding
import org.obesifix.obesifix.ui.history.HistoryViewModel

class HistoryAdapter(private val historyViewModel: HistoryViewModel):
    PagingDataAdapter<HistoryNutrition, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data, historyViewModel)
        }
    }

    class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryNutrition, historyViewModel: HistoryViewModel) {
            with(binding){
                tvFood.text = data.foodname
//                val kcal = data.calorie.div(1000).toString()
                tvCal.text = data.calorie.toString()
                tvFat.text =data.fat.toString()
                tvProtein.text = data.protein.toString()
                tvCarb.text = data.carbohydrate.toString()
                imgDelete.setOnClickListener {
                    val id = data.id
                    //historyViewModel.removeHistoryNutritionTodayById(id)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryNutrition>() {
            override fun areItemsTheSame(oldItem: HistoryNutrition, newItem: HistoryNutrition): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryNutrition, newItem: HistoryNutrition): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}