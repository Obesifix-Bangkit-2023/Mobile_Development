//package org.obesifix.obesifix.ui.home
//
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import org.obesifix.obesifix.databinding.ItemColHomeRecommendationBinding
//import org.obesifix.obesifix.ui.detail.DetailActivity
//
//// to connect between fragment home recycler view that can move to detail activity and item_col_home_recommendation
//class ListRecommendationAdapter(private val listRecommendation:List<RecommendationResponseItem>):
//    RecyclerView.Adapter<ListRecommendationAdapter.ListViewHolder>(){
//    class ListViewHolder(var binding: ItemColHomeRecommendationBinding): RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
//        val binding = ItemColHomeRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent,false)
//        return ListViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
//        listRecommendation[position].let{ item ->
//            Glide.with(holder.binding.root.context)
//                .load(item.)
//                .into(holder.binding.imgCal)
//            holder.binding.tvTitleCal.text = item.
//            holder.binding.tvCalDesc.text = item.
//        }
//        holder.itemView.setOnClickListener{
//            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
//            intentDetail.putExtra(DetailActivity. ,listRecommendation[holder.adapterPosition].)
//            holder.itemView.context.startActivity(intentDetail)
//        }
//    }
//
//    override fun getItemCount() = listRecommendation.size
//}