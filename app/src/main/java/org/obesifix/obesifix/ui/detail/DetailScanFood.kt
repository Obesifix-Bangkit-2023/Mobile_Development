package org.obesifix.obesifix.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityDetailScanFoodBinding
import java.text.DecimalFormat

class DetailScanFood : AppCompatActivity() {
    companion object {
        const val EXTRA_IMAGE = "extra_image"
        const val EXTRA_NAME_FOOD = "extra_name_food"
        const val EXTRA_SERVING = "extra_serving"
        const val EXTRA_CALORIE = "extra_calorie"
        const val EXTRA_FAT = "extra_fat"
        const val EXTRA_PROTEIN = "extra_protein"
        const val EXTRA_CARBOHYDRATE = "extra_carbohydrate"
        const val EXTRA_DESCRIPTION = "extra_description"
    }

    private lateinit var binding: ActivityDetailScanFoodBinding
    val decimalFormat = DecimalFormat("#.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressed() }

        val image = intent.getStringExtra(EXTRA_IMAGE)

        var nameFood = intent.getStringExtra(EXTRA_NAME_FOOD)
        nameFood = "$nameFood"

        val servingInt = intent.getIntExtra(EXTRA_SERVING, 0)
        val serving = servingInt.toString()

        val calorie = intent.getDoubleExtra(EXTRA_CALORIE, 0.0)
        val totalCalorie = calorie * servingInt
        val totalCalorieString = decimalFormat.format(totalCalorie)

        val fat = intent.getDoubleExtra(EXTRA_FAT, 0.0)
        val totalFat = fat * servingInt
        val totalFatString = decimalFormat.format(totalFat)

        val protein = intent.getDoubleExtra(EXTRA_PROTEIN, 0.0)
        val totalProtein = protein * servingInt
        val totalProteinString = decimalFormat.format(totalProtein)

        val carbohydrate = intent.getDoubleExtra(EXTRA_CARBOHYDRATE, 0.0)
        val totalCarbohydrate = carbohydrate * servingInt
        val totalCarbohydrateString = decimalFormat.format(totalCarbohydrate)

        var description = intent.getStringExtra(EXTRA_DESCRIPTION)
        description = "$description"

        binding.apply {
            imageView.setImageURI(Uri.parse(image))
            tvnameFood.text = nameFood
            tvServing.text = serving
            tvCalorie.text = totalCalorieString
            tvFat.text = totalFatString
            tvProtein.text = totalProteinString
            tvCarbo.text = totalCarbohydrateString
            tvDesc.text = description
        }

        }
    }
