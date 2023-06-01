package org.obesifix.obesifix.ui.scan

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import okhttp3.MultipartBody
import org.obesifix.obesifix.databinding.FragmentScanBinding
import org.obesifix.obesifix.network.PredictionResponse
import org.obesifix.obesifix.network.body.PredictionRequestBody
import org.obesifix.obesifix.ui.detail.DetailScanFood
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ScanFragment : Fragment(), PredictionRequestBody.UploadCallback {

    companion object {
        private const val REQUEST_IMAGE_PICKER_CODE = 1
        private const val REQUEST_CAMERA_CODE = 2
        private const val PERMISSION_CAMERA_CODE = 102
    }

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!

    private lateinit var currentPhotoPath: String
    private var selectedImage: Uri? = null

    private val initialProgress = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnCamera.setOnClickListener { askCameraPermission() }
            btnGallery.setOnClickListener { openImageChooser() }
            btnUpload.setOnClickListener { uploadImage() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun uploadImage() {
        if (selectedImage == null) {
            binding.root.snackbar("Select an Image First")
            return
        }

        val parcelFileDescriptor =
            requireContext().contentResolver.openFileDescriptor(selectedImage!!, "r", null)
                ?: return
        val file = File(requireContext().cacheDir, requireContext().contentResolver.getFileName(selectedImage!!))
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        binding.progressBar.progress = 0
        val body = PredictionRequestBody(file, "image", this)

        Api().predictFood(
            MultipartBody.Part.createFormData("image", file.name, body)
        ).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                binding.progressBar.progress = 100
                Log.d("upload", "name: {${response.body()?.name}}")
                Log.d("upload", "serving: {${response.body()?.serving}}")
                Log.d("upload", "calorie: {${response.body()?.calorie}}")
                Log.d("upload", "fat: {${response.body()?.fat}}")
                Log.d("upload", "protein: {${response.body()?.protein}}")
                Log.d("upload", "carbohydrate: {${response.body()?.carbohydrate}}")
                Log.d("upload", "description: {${response.body()?.description}}")

                val nameFood = response.body()?.name.toString()
                val serving = response.body()?.serving.toString()
                val calorie = response.body()?.calorie.toString()
                val fat = response.body()?.fat.toString()
                val protein = response.body()?.protein.toString()
                val carbohydrate = response.body()?.carbohydrate.toString()
                val description = response.body()?.description.toString()

                Intent(requireContext(), DetailScanFood::class.java).also {
                    it.putExtra(DetailScanFood.EXTRA_IMAGE, selectedImage.toString())
                    it.putExtra(DetailScanFood.EXTRA_NAME_FOOD, nameFood)
                    it.putExtra(DetailScanFood.EXTRA_SERVING, serving)
                    it.putExtra(DetailScanFood.EXTRA_CALORIE, calorie)
                    it.putExtra(DetailScanFood.EXTRA_FAT, fat)
                    it.putExtra(DetailScanFood.EXTRA_PROTEIN, protein)
                    it.putExtra(DetailScanFood.EXTRA_CARBOHYDRATE, carbohydrate)
                    it.putExtra(DetailScanFood.EXTRA_DESCRIPTION, description)
                    startActivity(it)
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                binding.root.snackbar(t.message!!)
                Log.d("upload", "onFailure: ${t.message}")
            }
        })
    }

    private fun openImageChooser() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/jpg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, REQUEST_IMAGE_PICKER_CODE)
        }
    }

    private fun askCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CAMERA_CODE
            )
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CAMERA_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Camera Permission is Required to Use the Camera",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @Throws(IOException::class)
    private fun createPhotoFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply { currentPhotoPath = absolutePath }
    }

    private fun openCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { imageCaptureIntent ->
            imageCaptureIntent.resolveActivity(requireContext().packageManager)?.also {
                val photoFile: File? = try {
                    createPhotoFile()
                } catch (e: IOException) {
                    // Error occurred while creating the File
                    null
                }

                photoFile?.also {
                    selectedImage = FileProvider.getUriForFile(
                        requireContext(),
                        "org.obesifix.obesifix.file-provider",
                        it
                    )

                    imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImage)
                    startActivityForResult(imageCaptureIntent, REQUEST_CAMERA_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICKER_CODE -> {
                    selectedImage = data?.data
                    binding.imageView.setImageURI(selectedImage)
                }
                REQUEST_CAMERA_CODE -> {
                    binding.imageView.setImageURI(selectedImage)
                }
            }
        }
    }

    override fun onProgressUpdate(percentage: Int) {
        binding.progressBar.progress = percentage
    }

    override fun onResume() {
        super.onResume()
        binding.progressBar.progress = initialProgress
    }
}