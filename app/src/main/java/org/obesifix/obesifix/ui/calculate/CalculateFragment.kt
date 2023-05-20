package org.obesifix.obesifix.ui.calculate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.obesifix.obesifix.R

class CalculateFragment : Fragment() {

    companion object {
        fun newInstance() = CalculateFragment()
    }

    private lateinit var viewModel: CalculateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calculate, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculateViewModel::class.java)
        // TODO: Use the ViewModel
    }

}