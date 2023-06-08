package org.obesifix.obesifix.ui.about

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.obesifix.obesifix.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }
}