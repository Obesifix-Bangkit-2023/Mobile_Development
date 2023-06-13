package org.obesifix.obesifix.ui.detail

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.obesifix.obesifix.databinding.ActivityDetailScanFoodBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressed() }

        val image = intent.getStringExtra(EXTRA_IMAGE)

        var nameFood = intent.getStringExtra(EXTRA_NAME_FOOD)
        nameFood = "$nameFood"

        val serving = intent.getDoubleExtra(EXTRA_SERVING, 0.0)
        val servingString = serving.toString()

        val calorie = intent.getDoubleExtra(EXTRA_CALORIE, 0.0)
        val calorieString = calorie.toString()

        val fat = intent.getDoubleExtra(EXTRA_FAT, 0.0)
        val fatString = fat.toString()

        val protein = intent.getDoubleExtra(EXTRA_PROTEIN, 0.0)
        val proteinString = protein.toString()

        val carbohydrate = intent.getDoubleExtra(EXTRA_CARBOHYDRATE, 0.0)
        val carbohydrateString = carbohydrate.toString()

        var description = intent.getStringExtra(EXTRA_DESCRIPTION)
        description = "$description"

        binding.apply {
            imageView.setImageURI(Uri.parse(image))
            tvnameFood.text = nameFood
            tvServing.text = servingString
            tvCalorie.text = calorieString
            tvFat.text = fatString
            tvProtein.text = proteinString
            tvCarbo.text = carbohydrateString
            tvDesc.text = description
        }
    }
}