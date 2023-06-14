package org.obesifix.obesifix.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.obesifix.obesifix.MainActivity
import org.obesifix.obesifix.databinding.ActivityDetailBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.FoodListItem
import org.obesifix.obesifix.preference.UserPreference
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var data: FoodListItem? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        detailViewModel =
            ViewModelProvider(this,
            ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore), application)
            )[DetailViewModel::class.java]
        auth = Firebase.auth
        data = intent.getParcelableExtra(EXTRA_ID)
        setupAction()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener { onBackPressed() }

        binding.tvNameRecommendation.text = data?.name ?: ""
        Glide.with(binding.root.context)
            .load(data?.image)
            .into(binding.imgRecommendation)
        binding.tvCalDesc.text = "${data?.calorie.toString()} Kcal"
        binding.tvFatDesc.text = "${data?.fat.toString()} g"
        binding.tvProteinDesc.text = "${data?.protein.toString()} g"
        binding.tvCarboDesc.text = "${data?.carbohydrate.toString()} g"
        binding.tvTagDesc.text = data?.keyword

        val userid = auth.currentUser?.uid
        val foodname = data?.name
        val calorie = data?.calorie
        val fat = data?.fat
        val protein = data?.protein
        val carbohydrate = data?.carbohydrate
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(calendar.time)

        binding.addButton.setOnClickListener {
            Log.d("DATA PARCELDCT", "${data?.calorie}, ${data?.image}")

            if (userid != null && calorie != null && fat != null && protein != null && carbohydrate != null && foodname != null) {
                detailViewModel.addNutrition(userid,foodname,calorie,fat,protein,carbohydrate,currentDate)
                Log.d("DB DETAIL", "INSIDE")
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("data", data)
            startActivity(intent)
        }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
    }

}