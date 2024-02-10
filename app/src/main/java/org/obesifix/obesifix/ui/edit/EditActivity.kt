package org.obesifix.obesifix.ui.edit

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.obesifix.obesifix.databinding.ActivityEditBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.payload.EditBody
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginActivity
import org.obesifix.obesifix.ui.preference.FoodOptions

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var editViewModel: EditViewModel
    private var activitySelectedOption: String = ""
    private lateinit var userPreference: UserPreference
    private var selectedFoodItems:String = ""

    //text watcher
    private var isNameEmpty = false
    private var isAgeEmpty = false
    private var isWeightEmpty = false
    private var isHeightEmpty = false
    private var isActivitySelected = false
    private var isFoodItemsSelected = false

    //don't forget to change the height from cm to m
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide() // Hide the default action bar
        editViewModel =
            ViewModelProvider(
                this,
                ViewModelFactory(
                    applicationContext,
                    UserPreference.getInstance(applicationContext.dataStore),
                    application
                )
            )[EditViewModel::class.java]
        userPreference = UserPreference.getInstance(applicationContext.dataStore)

        setupAction()
    }

    private fun setupAction() {
        editViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        binding.submitButton.isEnabled = false
        updateButtonState()

        val activityOptions = arrayOf("Sedentary", "Low activity", "Active", "Very active")

        val adapterActivity =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, activityOptions)
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerActivities.adapter = adapterActivity

        binding.spinnerActivities.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    activitySelectedOption = activityOptions[position].lowercase()
                    updateButtonState()
                    textWatcher.afterTextChanged(null)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    updateButtonState()
                }
            }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.ageEditText.addTextChangedListener(textWatcher)
        binding.weightEditText.addTextChangedListener(textWatcher)
        binding.heightEditText.addTextChangedListener(textWatcher)

        val foodOptions = FoodOptions.foodOptions
        foodOptions.forEach { item ->
            val chip = Chip(this)
            chip.text = item
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedFoodItems += if (selectedFoodItems.isNotEmpty()) {
                        ",$item"
                    } else {
                        item
                    }
                    updateButtonState()
                } else {
                    selectedFoodItems = selectedFoodItems.replace(", $item", "").replace(item, "")
                    updateButtonState()
                }
                // Trigger textWatcher to reevaluate the button's enabled state
                textWatcher.afterTextChanged(null)
            }
            updateButtonState()
            binding.chipEating.addView(chip)
        }

        // To preselect items:
        selectedFoodItems.forEach { item ->
            val chip = binding.chipEating.findViewWithTag<Chip>(item)
            chip?.isChecked = true
            updateButtonState()
        }


        binding.submitButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val age = binding.ageEditText.text.toString().toInt()
            val heightCM = binding.heightEditText.text.toString().toFloat()
            val heightM = heightCM.toString().toFloat().div(100)
            val weight = binding.weightEditText.text.toString().toFloat()
            val activityLevel = activitySelectedOption
            val selectedFoodItemsArray = selectedFoodItems
            Log.d("sfood", "$selectedFoodItemsArray")
            val editData =
                EditBody(
                    name, age, heightM, weight, activityLevel, selectedFoodItemsArray
                )

            lifecycleScope.launch {
                val token: String = userPreference.getAccessTokenUser().first()
                val id: String = userPreference.getUserId().first()
                Log.d(ContentValues.TAG, "token profile: $token")
                editViewModel.requestUpdateProfile(token = token, editBody = editData, userId = id)
                editViewModel.isNavigate.observe(this@EditActivity) { shouldNavigate ->
                    if (shouldNavigate) {
                        this@EditActivity.lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                userPreference.logout()
                            }
                        }
                        startActivity(Intent(this@EditActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }


            editViewModel.editResponse.observe(this) { response ->
                if (response.statusCode == 200) {
                   finish()
                }
            }
        }

    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            isNameEmpty = binding.nameEditText.text.isNullOrBlank()
            isAgeEmpty = binding.ageEditText.text.isNullOrBlank()
            isWeightEmpty = binding.weightEditText.text.isNullOrBlank()
            isHeightEmpty = binding.heightEditText.text.isNullOrBlank()
            isActivitySelected = activitySelectedOption.isBlank()
            isFoodItemsSelected = selectedFoodItems.isEmpty()
            updateButtonState()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun updateButtonState() {
        binding.submitButton.isEnabled = !isAgeEmpty && !isWeightEmpty && !isHeightEmpty
                && !isActivitySelected && !isFoodItemsSelected && !isNameEmpty
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}