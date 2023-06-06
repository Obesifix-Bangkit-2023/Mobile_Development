package org.obesifix.obesifix.ui.about

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityAboutBinding
import org.obesifix.obesifix.databinding.ActivityListBinding

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