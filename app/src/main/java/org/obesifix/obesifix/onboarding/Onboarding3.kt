package org.obesifix.obesifix.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import org.obesifix.obesifix.R
import org.obesifix.obesifix.ui.login.LoginActivity


class Onboarding3 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_onboarding3, container, false)
        val toLoginPage = fragment.findViewById<Button>(R.id.btnGetStarted)
        toLoginPage.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        return fragment
    }
}