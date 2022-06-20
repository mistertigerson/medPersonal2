package com.test.medpersonal.presentation.fragments.adminPanel

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentAdminBinding


class AdminFragment : Fragment(R.layout.fragment_admin) {

    private val binding: FragmentAdminBinding by viewBinding()
    private val adminAdapter by lazy {
        AdminAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
    }

    private fun initRv() {
        binding.rvAdmin.apply {
            adapter = adminAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}