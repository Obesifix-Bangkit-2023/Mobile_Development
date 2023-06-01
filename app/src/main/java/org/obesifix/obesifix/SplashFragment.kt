package org.obesifix.obesifix

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.obesifix.obesifix.onboarding.ViewPagerFragment
import org.obesifix.obesifix.ui.login.LoginActivity


class SplashFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_splash)

        val user = FirebaseAuth.getInstance().currentUser
        Handler().postDelayed({
            // Kondisi if aktif jika sudah pernah membuka app
            if (user != null) {
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                startActivity(Intent(this, ViewPagerFragment::class.java))
            }
            finish()
        }, 4000)
    }
}