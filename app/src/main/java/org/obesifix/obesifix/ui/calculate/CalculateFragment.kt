package org.obesifix.obesifix.ui.calculate

import android.app.Application
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.FragmentCalculateBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.history.HistoryActivity
import java.text.SimpleDateFormat
import java.util.*

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculateFragment : Fragment() {

    private lateinit var calculateViewModel: CalculateViewModel
    private var _binding: FragmentCalculateBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPreference: UserPreference
    private val calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)

    //selected Date
    private var selectedDate = calendar.time

    //selected Date formatted
    private var formattedDate: String = ""

    //current Date
    private val currentDate = calendar.time

    //format Date
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    //nutrition Summary
    private var nutriSummary: Float? = 0f

    //nutrition Data
    private var nutriData: Float? = 0f

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireContext().applicationContext as Application
        calculateViewModel = ViewModelProvider(
            this,
            ViewModelFactory(
                requireContext(),
                UserPreference.getInstance(requireContext().dataStore),
                application
            )
        )[CalculateViewModel::class.java]
        userPreference = UserPreference.getInstance(requireContext().dataStore)
        _binding = FragmentCalculateBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentCalculateBinding.bind(view)
        setupAction()
    }

    private fun setupAction() {
        calculateViewModel.status.observe(viewLifecycleOwner) { status ->
            binding.tvStatusDesc.text = status
        }

        binding.cardView3.visibility = View.GONE

        formattedDate = dateFormat.format(currentDate)
        binding.tvDate.text = Editable.Factory.getInstance().newEditable(formattedDate)

        binding.tvDate.setOnClickListener { showDatePickerDialog() }

        lifecycleScope.launch {
            val token: String = userPreference.getAccessTokenUser().first()
            val id: String = userPreference.getUserId().first()
            Log.d("token home", "token home $token")
            Log.d("id home", "id home $id")

            calculateViewModel.getDataNutritionByIdAndDate(id, formattedDate)
            calculateViewModel.getUserData(token, id)
            calculateViewModel.userData.observe(viewLifecycleOwner){ userData ->
                binding.tvNameDesc.text = userData.userData?.name
            }
            Log.d(
                "Date",
                "sd:$selectedDate, cd:$currentDate"
            )
            if (selectedDate.equals(currentDate)) {
                currentDateData()
            }
        }

        calculateViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }


        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        binding.addButton.setOnClickListener {
            val desiredTabId = R.id.navigation_scan
            bottomNavigationView.selectedItemId = desiredTabId
        }
        warningShow()
        binding.imgHistory.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                calendar.set(year, monthOfYear, dayOfMonth)
                selectedDate = calendar.time

                formattedDate = selectedDate.let { dateFormat.format(it) }.toString()

                binding.tvDate.text = Editable.Factory.getInstance().newEditable(formattedDate)

                lifecycleScope.launch {
                    val token: String = userPreference.getAccessTokenUser().first()
                    val id: String = userPreference.getUserId().first()
                    Log.d("token home", "token home $token")
                    Log.d("id home", "id home $id")

                    calculateViewModel.getDataNutritionByIdAndDate(id, formattedDate)
                    if (selectedDate.equals(currentDate)) {
                        currentDateData()
                    } else {
                        //Current Data
                        calculateViewModel.nutritionDataDb.observe(viewLifecycleOwner) { nutritionSummary ->
                            Log.d(
                                "nutritionSummary",
                                "data nutritionSummary $nutritionSummary"
                            )
                            binding.tvCalDesc.text =
                                "%.1f".format(nutritionSummary.totalCalorie)
                            binding.tvFatDesc.text =
                                "%.1f".format(nutritionSummary.totalFat)
                            binding.tvProteinDesc.text =
                                "%.1f".format(nutritionSummary.totalProtein)
                            binding.tvCarboDesc.text =
                                "%.1f".format(nutritionSummary.totalCarbohydrate)
                        }

                        val anotherDate = resources.getString(R.string.another_current)
                        binding.tvCalNeed.text = anotherDate
                        binding.tvFatNeed.text = anotherDate
                        binding.tvProteinNeed.text = anotherDate
                        binding.tvCarboNeed.text = anotherDate
                    }
                    warningShow()
                }
            },
            year,
            month,
            day
        )
        // Set the maximum date to the current date
        val currentDate = Calendar.getInstance()
        datePickerDialog.datePicker.maxDate = currentDate.timeInMillis
        datePickerDialog.show()
    }

    private fun currentDateData() {
        //Need Data
        calculateViewModel.nutritionLiveData.observe(viewLifecycleOwner) { nutritionData ->
            Log.d("nutritionData", "data nutritionData $nutritionData")
            nutriData = nutritionData.calNeed
            binding.tvCalNeed.text =
                "from ${"%.1f".format(nutritionData.calNeed)} Kcal"
            binding.tvFatNeed.text =
                "from ${"%.1f".format(nutritionData.fatNeed)} g"
            binding.tvProteinNeed.text =
                "from ${"%.1f".format(nutritionData.proteinNeed)} g"
            binding.tvCarboNeed.text =
                "from ${"%.1f".format(nutritionData.carbNeed)} g"
            warningShow()
        }

        //Current Data
        calculateViewModel.nutritionDataDb.observe(viewLifecycleOwner) { nutritionSummary ->
            Log.d(
                "nutritionSummary",
                "data nutritionSummary $nutritionSummary"
            )
            nutriSummary = nutritionSummary.totalCalorie
            binding.tvCalDesc.text =
                "%.1f".format(nutritionSummary.totalCalorie)
            binding.tvFatDesc.text =
                "%.1f".format(nutritionSummary.totalFat)
            binding.tvProteinDesc.text =
                "%.1f".format(nutritionSummary.totalProtein)
            binding.tvCarboDesc.text =
                "%.1f".format(nutritionSummary.totalCarbohydrate)
            warningShow()
        }
        warningShow()
    }

    private fun warningShow() {
        val nutriSummary: Float? = nutriSummary
        val nutriData: Float? = nutriData

        if (nutriSummary != null && nutriData != null) {
            if (nutriSummary > nutriData) {
                binding.cardView3.visibility = View.VISIBLE
            } else {
                binding.cardView3.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Reset selectedDate to the current date
        formattedDate = dateFormat.format(currentDate)
        binding.tvDate.text = Editable.Factory.getInstance().newEditable(formattedDate)
    }


}
