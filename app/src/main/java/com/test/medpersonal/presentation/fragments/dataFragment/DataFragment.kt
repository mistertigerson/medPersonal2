package com.test.medpersonal.presentation.fragments.dataFragment

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentDataBinding
import com.test.medpersonal.domain.models.Users


class DataFragment : Fragment(R.layout.fragment_data) {
    private val binding: FragmentDataBinding by viewBinding()
    private lateinit var model: Users /// HomeModel
    private lateinit var database: DatabaseReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        val userReference = Firebase.database.reference
            .child("users").child(user?.uid.toString())


        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val users_Model = dataSnapshot.getValue(Users::class.java)

                binding.etAddName.setText(users_Model?.name)
                binding.etAddSecondName.setText(users_Model?.secondName)
                binding.etAddLastName.setText(users_Model?.lastName)
                binding.etAddNumber.setText(users_Model?.phone)
                binding.etAddSpecific.setText(users_Model?.specific)
                binding.etAddCompany.setText(users_Model?.company)


            }

            override fun onCancelled(error: DatabaseError) {


            }
        }
        userReference.addListenerForSingleValueEvent(userListener)

        binding.btnSaveData.setOnClickListener {
            addData()
            findNavController().navigateUp()
        }
    }

    private fun addData() {
        lateinit var database: DatabaseReference
        database = Firebase.database.reference

        val auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid.toString()
        model = Users(
            binding.etAddLastName.text.toString(),
            binding.etAddNumber.text.toString(),
            binding.etAddName.text.toString(),
            binding.etAddSecondName.text.toString(),
            binding.etAddSpecific.text.toString(),
            binding.etAddCompany.text.toString(),
            auth.currentUser?.uid.toString(),
        )
        val profileValues = model.toMap()

        val childUpdates = hashMapOf<String, Any>(
            "/users/$userId" to profileValues
        )

        database.updateChildren(childUpdates)

    }

}