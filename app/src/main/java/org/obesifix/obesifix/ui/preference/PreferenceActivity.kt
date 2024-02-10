package org.obesifix.obesifix.ui.preference

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.obesifix.obesifix.MainActivity
import org.obesifix.obesifix.databinding.ActivityPreferenceBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.payload.RegisterBody
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.login.LoginViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class PreferenceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPreferenceBinding
    private var selectedGender: String = ""
    private var activitySelectedOption: String =""
    private lateinit var auth: FirebaseAuth
    private var selectedFoodItems = mutableListOf<String>()

    //text watcher
    private var isNameEmpty = false
    private var isEmailEmpty = false
    private var isPasswordEmpty = false
    private var isAgeEmpty = false
    private var isWeightEmpty = false
    private var isHeightEmpty = false
    private var isGenderSelected = false
    private var isActivitySelected = false
    private var isFoodItemsSelected = false

    private lateinit var preferenceViewModel: PreferenceViewModel
    //don't forget to change the height from cm to m
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        preferenceViewModel =
            ViewModelProvider(this,
                ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore),application)
            )[PreferenceViewModel::class.java]

        auth = Firebase.auth
        setupAction()
    }

    private fun setupAction(){

        preferenceViewModel.isLoading.observe(this){
            showLoading(it)
        }

        binding.submitButton.isEnabled = false
        updateButtonState()
        binding.radioGroupGender.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedGender = radioButton.text.toString()
            updateButtonState()
            // Trigger the afterTextChanged callback of the textWatcher
            textWatcher.afterTextChanged(null)
        }

        val activityOptions = arrayOf("Sedentary", "Low activity", "Active", "Very active")

        val adapterActivity = ArrayAdapter(this, android.R.layout.simple_spinner_item, activityOptions)
        adapterActivity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerActivities.adapter = adapterActivity

        binding.spinnerActivities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                activitySelectedOption = activityOptions[position].lowercase()
                updateButtonState()
                textWatcher.afterTextChanged(null)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateButtonState()
            }
        }

        binding.ageEditText.addTextChangedListener(textWatcher)
        binding.weightEditText.addTextChangedListener(textWatcher)
        binding.heightEditText.addTextChangedListener(textWatcher)
        binding.nameEditText.addTextChangedListener(textWatcher)
        binding.emailEditText.addTextChangedListener(textWatcher)
        binding.passwordEditText.addTextChangedListener(textWatcher)

        val foodOptions = FoodOptions.foodOptions
        foodOptions.forEach { item ->
            val chip = Chip(this)
            chip.text = item
            chip.isCheckable = true
            chip.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedFoodItems.add(item)
                    updateButtonState()
                } else {
                    selectedFoodItems.remove(item)
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
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val age = binding.ageEditText.text.toString().toInt()
            val gender = selectedGender.toLowerCase()
            val heightCM = binding.heightEditText.text.toString().toFloat()
            val heightM = heightCM.toString().toFloat().div(100)
            val weight = binding.weightEditText.text.toString().toFloat()
            val activityLevel = activitySelectedOption
            val selectedFoodItemsArray = selectedFoodItems.joinToString(",")
            Log.d("sfood","$selectedFoodItemsArray")
            val registerData = RegisterBody(
                name, email, password, age, gender, heightM, weight, activityLevel, selectedFoodItemsArray
            )

            preferenceViewModel.requestRegister(registerData)
            preferenceViewModel.preferenceResponse.observe(this){ response ->
                if(response.statusCode == 201){
                    Toast.makeText(this@PreferenceActivity, "${response.message}", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@PreferenceActivity, "${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            isNameEmpty = binding.nameEditText.text.isNullOrBlank()
            isEmailEmpty = binding.emailEditText.text.isNullOrBlank()
            isPasswordEmpty = binding.passwordEditText.text.isNullOrBlank()
            isWeightEmpty = binding.weightEditText.text.isNullOrBlank()
            isHeightEmpty = binding.heightEditText.text.isNullOrBlank()
            isGenderSelected =  selectedGender.isBlank()
            isActivitySelected = activitySelectedOption.isBlank()
            isFoodItemsSelected = selectedFoodItems.isEmpty()
            updateButtonState()
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun updateButtonState() {
        binding.submitButton.isEnabled = !isNameEmpty&& !isEmailEmpty && !isPasswordEmpty && !isAgeEmpty && !isWeightEmpty && !isHeightEmpty && !isGenderSelected
                && !isActivitySelected && !isFoodItemsSelected
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
