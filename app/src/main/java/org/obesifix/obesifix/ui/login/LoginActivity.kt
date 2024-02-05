package org.obesifix.obesifix.ui.login
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.ktx.auth
import org.obesifix.obesifix.MainActivity
import org.obesifix.obesifix.R
import org.obesifix.obesifix.databinding.ActivityLoginBinding
import org.obesifix.obesifix.factory.ViewModelFactory
import org.obesifix.obesifix.network.payload.LoginBody
import org.obesifix.obesifix.preference.UserPreference
import org.obesifix.obesifix.ui.preference.PreferenceActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        loginViewModel =
            ViewModelProvider(this,
                ViewModelFactory(applicationContext, UserPreference.getInstance(applicationContext.dataStore), application)
            )[LoginViewModel::class.java]

        val registerText = "Don\'t have account ? Register"
        val spannableString = SpannableString(registerText)

        val blueColor = ContextCompat.getColor(this, R.color.primaryblue)
        spannableString.setSpan(ForegroundColorSpan(blueColor), 21, registerText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.registerTextView.text = spannableString
        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this@LoginActivity, PreferenceActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            requestLogin()
        }

    }

    private fun requestLogin(){
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        val loginBody = LoginBody(
            email, password
        )
        loginViewModel.requestLogin(loginBody)
        loginViewModel.loginResponse.observe(this){ response ->
            if(response.statusCode == 200){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this@LoginActivity, "${response.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}