package com.test.medpersonal.presentation.fragments.groupFragment

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentGroupBinding
import com.test.medpersonal.domain.models.OtherUserModel
import com.test.medpersonal.domain.models.Post


class GroupFragment : Fragment(R.layout.fragment_group) {

    private val binding: FragmentGroupBinding by viewBinding()
    private lateinit var database: DatabaseReference
    private lateinit var userReference: DatabaseReference
    private var list: ArrayList<OtherUserModel> = arrayListOf()
    private val adapter: UserAdapter by lazy {
        UserAdapter()
    }
    private lateinit var to: String
    private lateinit var listData: OtherUserModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userReference = Firebase.database.reference.child("users")

        setData()
        setupUi()
        setUpListener()

    }

    private fun setData() {

        val user = FirebaseAuth.getInstance().currentUser

        Log.e("TAG", "onViewCreated: ${user?.uid}")
        Log.e("TAG", "onViewCreated: ${userReference}")


        val userListener = object : ValueEventListener {
            override fun onDataChange(
                dataSnapshot: DataSnapshot
            ) {
                // Get Post object and use the values to update the UI

            }


            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
                Toast.makeText(
                    context, "Failed to load post.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        //   userReference.addValueEventListener(userListener)
        onChangeListener(userReference)

    }

    private fun setupUi() {

        binding.rvUser.apply {
            adapter = this@GroupFragment.adapter
        }
    }

    private fun setUpListener() {
        adapter.onItemClick = {

            findNavController().navigate(
                GroupFragmentDirections.actionGroupFragmentToChatFragment(
                    it.uid.toString()
                )
            )

        }
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (s in snapshot.children) {
                    val otherUserModel = s.getValue(OtherUserModel::class.java)
                    if (otherUserModel != null) {
                        if (otherUserModel.uid != FirebaseAuth.getInstance().currentUser?.uid)
                            list.add(otherUserModel)
                    }
                }
                adapter.submitList(list)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}