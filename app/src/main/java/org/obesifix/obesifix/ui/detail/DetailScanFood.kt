package org.obesifix.obesifix.ui.detail

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.obesifix.obesifix.R
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

        val image = intent.getStringExtra(EXTRA_IMAGE)

        var nameFood = intent.getStringExtra(EXTRA_NAME_FOOD)
        nameFood = "$nameFood"

        var serving = intent.getStringExtra(EXTRA_SERVING)
        serving = "$serving"

        var calorie = intent.getStringExtra(EXTRA_CALORIE)
        calorie = "$calorie"

        var fat = intent.getStringExtra(EXTRA_FAT)
        fat = "$fat"

        var protein = intent.getStringExtra(EXTRA_PROTEIN)
        protein = "$protein"

        var carbohydrate = intent.getStringExtra(EXTRA_CARBOHYDRATE)
        carbohydrate = "$carbohydrate"

        var description = intent.getStringExtra(EXTRA_DESCRIPTION)
        description = "$description"

        binding.apply {
            imageView.setImageURI(Uri.parse(image))
            tvnameFood.text = nameFood
            //tvServing.text = serving
            tvCalorie.text = calorie
            tvFat.text = fat
            tvProtein.text = protein
            tvCarbo.text = carbohydrate
            tvDesc.text = description
        }
    }
}