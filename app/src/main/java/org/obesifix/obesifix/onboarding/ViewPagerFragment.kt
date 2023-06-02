package org.obesifix.obesifix.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import org.obesifix.obesifix.R


class ViewPagerFragment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.fragment_view_pager)

        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val fragmentList = arrayListOf<Fragment>(
            Onboarding1(),
            Onboarding2(),
            Onboarding3()
        )

        val adapter = ViewPagerAdapter(fragmentList, supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        val wormDotsIndicator: WormDotsIndicator = findViewById(R.id.worm_dots_indicator)
        wormDotsIndicator.attachTo(viewPager)
    }
}