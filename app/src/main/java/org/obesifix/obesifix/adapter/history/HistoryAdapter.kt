package org.obesifix.obesifix.adapter.history

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.obesifix.obesifix.database.entity.HistoryNutrition
import org.obesifix.obesifix.databinding.ItemHistoryBinding
import org.obesifix.obesifix.ui.history.HistoryViewModel

class HistoryAdapter(private val historyViewModel: HistoryViewModel, private val context: Context):
    PagingDataAdapter<HistoryNutrition, HistoryAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    interface OnRemoveClickListener {
        fun onRemoveClicked(historyNutrition: HistoryNutrition)
    }

    var onRemoveClickListener: OnRemoveClickListener? = null

    inner class MyViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HistoryNutrition) {
            with(binding){
                tvFood.text = data.foodname
                tvCal.text = data.calorie.toString()
                tvFat.text =data.fat.toString()
                tvProtein.text = data.protein.toString()
                tvCarb.text = data.carbohydrate.toString()
                imgDelete.setOnClickListener {
                    showRemoveConfirmationDialog(data)
                }
            }
        }

        private fun showRemoveConfirmationDialog(historyNutrition: HistoryNutrition) {
            val dialog = AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to remove this item?")
                .setPositiveButton("Remove") { dialog, _ ->
                    historyViewModel.removeHistoryNutritionTodayById(historyNutrition.id)
                    notifyItemRemoved(bindingAdapterPosition)
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()

            dialog.show()
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryNutrition>() {
            override fun areItemsTheSame(oldItem: HistoryNutrition, newItem: HistoryNutrition): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HistoryNutrition, newItem: HistoryNutrition): Boolean {
                return oldItem == newItem
            }
        }
    }
}