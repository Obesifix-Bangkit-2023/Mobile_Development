package org.obesifix.obesifix.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityDetailBinding
import org.obesifix.obesifix.databinding.ActivityLoginBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
        binding.tvNameRecommendation.text = ""

    }


}