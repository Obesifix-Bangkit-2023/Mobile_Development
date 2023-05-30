package org.obesifix.obesifix.ui.mentoring

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.obesifix.obesifix.R

class MentoringFragment : Fragment() {

    companion object {
        fun newInstance() = MentoringFragment()
    }

    private lateinit var viewModel: MentoringViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mentoring, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MentoringViewModel::class.java)
        // TODO: Use the ViewModel
    }

}