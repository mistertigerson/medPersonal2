package com.test.medpersonal.presentation.fragments.homeFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentHomeBinding
import com.test.medpersonal.domain.models.HomeModel


class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding()
    private val homeAdapter: HomeAdapter by lazy {
        HomeAdapter()
    }
    private var list: ArrayList<HomeModel> = ArrayList()
    private lateinit var model: HomeModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addNews.setOnClickListener {
            findNavController().navigate(R.id.newsDataFragment)
        }
        setupUI()

    }



    private fun setupUI() {
        val db = FirebaseFirestore.getInstance()
        db.collection("news")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    model = HomeModel(
                        text = document.data["text"].toString(),
                        title = document.data["title"].toString(),
                        image = document.data["image"].toString()
                    )
                    list.add(model)
                    homeAdapter.submitList(list)
                    homeAdapter.notifyDataSetChanged()
                    Log.d("TAG", "${document.id} => ${document.data["title"].toString()}")
                }
               
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents.", exception)
            }
        binding.recyclerSearch.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}