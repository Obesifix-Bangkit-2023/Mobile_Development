package org.obesifix.obesifix.ui.detail

import android.content.Intent
import android.nfc.NfcAdapter.EXTRA_ID
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import org.obesifix.obesifix.MainActivity
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityDetailBinding
import org.obesifix.obesifix.network.FoodListItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var data: FoodListItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        data = intent.getParcelableExtra(EXTRA_ID)
        setupAction()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.tvNameRecommendation.text = data?.name ?: ""
        Glide.with(binding.root.context)
            .load(data?.image)
            .into(binding.imgRecommendation)
        binding.tvCalDesc.text = "${data?.calorie.toString()} Kcal"
        binding.tvFatDesc.text = "${data?.fat.toString()} g"
        binding.tvProteinDesc.text = "${data?.protein.toString()} g"
        binding.tvCarboDesc.text = "${data?.carbohydrate.toString()} g"
        binding.tvTagDesc.text = data?.keyword
        binding.addButton.setOnClickListener {
            Log.d("DATA PARCELDCT", "${data?.calorie}, ${data?.image}")
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

}