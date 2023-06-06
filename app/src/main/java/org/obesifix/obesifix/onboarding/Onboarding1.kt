package org.obesifix.obesifix.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import org.obesifix.obesifix.R


class Onboarding1 : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment =  inflater.inflate(R.layout.fragment_onboarding1, container, false)
        val SkipTour = fragment.findViewById<TextView>(R.id.skip1)
        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
        SkipTour.setOnClickListener {
            viewPager?.currentItem = 2
        }
        return fragment
    }
}