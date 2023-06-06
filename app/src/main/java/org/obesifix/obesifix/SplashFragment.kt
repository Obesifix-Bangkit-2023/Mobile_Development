package org.obesifix.obesifix

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.obesifix.obesifix.databinding.FragmentSplashBinding
import org.obesifix.obesifix.onboarding.ViewPagerFragment


class SplashFragment : AppCompatActivity() {
    private lateinit var binding:FragmentSplashBinding
    private val delayMillis = 4000L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentSplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val user = FirebaseAuth.getInstance().currentUser
        Handler().postDelayed({
            // Kondisi if aktif jika sudah pernah membuka app
            if (user != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, ViewPagerFragment::class.java))
            }
            finish()
        }, delayMillis)
    }
}