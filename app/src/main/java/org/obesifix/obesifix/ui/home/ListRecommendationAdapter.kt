package org.obesifix.obesifix.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.obesifix.obesifix.databinding.ItemColHomeRecommendationBinding
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.ui.detail.DetailActivity

// to connect between fragment home recycler view that can move to detail activity and item_col_home_recommendation
class ListRecommendationAdapter(private val listRecommendation:List<FoodListItem>):
    RecyclerView.Adapter<ListRecommendationAdapter.ListViewHolder>(){
    class ListViewHolder(var binding: ItemColHomeRecommendationBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemColHomeRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        listRecommendation[position].let{ item ->
            Glide.with(holder.binding.root.context)
                .load(item.image)
                .into(holder.binding.imgCal)
            holder.binding.tvTitleCal.text = item.name
            val calKcal = item.calorie?.div(1000).toString()
            holder.binding.tvCalDesc.text = "$calKcal Kcal"
        }
        holder.itemView.setOnClickListener{
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.EXTRA_ID, listRecommendation[position])
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount() = 4
}