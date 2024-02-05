package org.obesifix.obesifix.ui.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import org.obesifix.obesifix.MainActivity
import org.obesifix.obesifix.databinding.ActivityDetailScanFoodBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.response.FoodListItem
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.scan.ScanViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
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

    private var data: FoodListItem? = null
    private lateinit var scanViewModel: ScanViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityDetailScanFoodBinding
    val decimalFormat = DecimalFormat("#.##")
    val calendar = Calendar.getInstance()
    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailScanFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressed() }

        scanViewModel =
            ViewModelProvider(this,
                ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore), application)
            )[ScanViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        val userid = auth.currentUser?.uid

        val image = intent.getStringExtra(EXTRA_IMAGE)

        var nameFood = intent.getStringExtra(EXTRA_NAME_FOOD)
        nameFood = "$nameFood"

        val servingInt = intent.getIntExtra(EXTRA_SERVING, 0)
        val serving = servingInt.toString()

        val calorie = intent.getDoubleExtra(EXTRA_CALORIE, 0.0)
        val totalCalorie = decimalFormat.format(calorie)

        //float function
        val calorie2 = intent.getDoubleExtra(EXTRA_CALORIE, 0.0)
        val calculateCalorie = try {
            (calorie2.toFloat())
        } catch (e: NumberFormatException) {
            0f
        }

        val fat = intent.getDoubleExtra(EXTRA_FAT, 0.0)
        val totalFat = decimalFormat.format(fat)

        //float function
        val fat2 = intent.getDoubleExtra(EXTRA_FAT, 0.0)
        val calculateFat = try {
            (fat2.toFloat())
        } catch (e: NumberFormatException) {
            0f
        }

        val protein = intent.getDoubleExtra(EXTRA_PROTEIN, 0.0)
        val totalProtein = decimalFormat.format(protein)

        //float function
        val protein2 = intent.getDoubleExtra(EXTRA_PROTEIN, 0.0)
        val calculateProtein = try {
            (protein2.toFloat())
        } catch (e: NumberFormatException) {
            0f
        }

        val carbohydrate = intent.getDoubleExtra(EXTRA_CARBOHYDRATE, 0.0)
        val totalCarbohydrate = decimalFormat.format(carbohydrate)

        //float function
        val carbo2 = intent.getDoubleExtra(EXTRA_CARBOHYDRATE, 0.0)
        val calculateCarbo = try {
            (carbo2.toFloat())
        } catch (e: NumberFormatException) {
            0f
        }

        var description = intent.getStringExtra(EXTRA_DESCRIPTION)
        description = "$description"

        binding.apply {
            imageView2.setImageURI(Uri.parse(image))
            tvnameFood.text = nameFood
            tvServing.text = serving
            tvCalorie.text = totalCalorie
            tvFat.text = totalFat
            tvProtein.text = totalProtein
            tvCarbo.text = totalCarbohydrate
            tvDesc.text = description
        }

        binding.btnAdd.setOnClickListener {
            data = FoodListItem(image)
            Log.d("DATA PARCELDCT", "${data?.calorie}, ${data?.image}")

            if (userid != null && calculateCalorie != null && calculateFat != null && calculateProtein != null && calculateCarbo != null && nameFood != null) {
                scanViewModel.addNutrition(userid,nameFood,calculateCalorie,calculateFat,calculateProtein,calculateCarbo,currentDate)
                Log.d("DB DETAIL", "INSIDE")
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }
}
