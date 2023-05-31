package org.obesifix.obesifix.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import org.obesifix.obesifix.databinding.ActivityDetailBinding
import org.obesifix.obesifix.network.FoodListItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var data: FoodListItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data = intent.getParcelableExtra(EXTRA_ID)!!
        setupAction()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.tvNameRecommendation.text = data.name
        Glide.with(binding.root.context)
            .load(data.image)
            .into(binding.imgCal)
        binding.tvCalDesc.text = data.calorie.toString()
        binding.tvFatDesc.text = data.fat.toString()
        binding.tvProteinDesc.text = data.protein.toString()
        binding.tvCarboDesc.text = data.carbohydrate.toString()
        binding.tvTagDesc.text = data.keyword
        binding.addButton.setOnClickListener {

        }

    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

}