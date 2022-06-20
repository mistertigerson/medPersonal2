package com.test.medpersonal.presentation.fragments.profileFragment

import android.content.ContentValues.TAG
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentProfileBinding
import com.test.medpersonal.domain.models.Users


class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val binding: FragmentProfileBinding by viewBinding()
    private lateinit var auth: FirebaseAuth
    lateinit var bMap: Bitmap
    lateinit var icon: BitmapDrawable
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var model: Users /// HomeModel
    private lateinit var database: DatabaseReference
    private lateinit var userReference: DatabaseReference
    private var userListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        userReference = Firebase.database.reference
            .child("users").child(auth.currentUser?.uid.toString())
//        lifecycleScope.launch {
//            bMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
//            icon = BitmapDrawable(resources, bMap)
//
//        }

//        binding.ivAvatar.setImageDrawable(icon)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser

        val userListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val users_Model = dataSnapshot.getValue(Users::class.java)
                //binding.ivAvatar.source = user_Model?.image
              //  val i = users_Model?.name
           //     Log.d(TAG, i.toString())
                binding.tvUsername.text = users_Model?.name
                binding.tvLastName.text = users_Model?.secondName
                binding.tvSecondName.text = users_Model?.lastName
                binding.tvPhone.text = users_Model?.phone
                binding.tvSpecific.text = users_Model?.specific
                binding.tvCompany.text = users_Model?.company


            }

            override fun onCancelled(error: DatabaseError) {


            }
        }
        userReference.addListenerForSingleValueEvent(userListener)
       /// userListener?.let { userReference.addValueEventListener(it) }
        binding.btnSignOut.setOnClickListener()
        {
            auth.signOut()
            findNavController().navigate(R.id.authFragment)
        }


        binding.btnAddData.setOnClickListener()
        {
            findNavController().navigate(R.id.dataFragment)
        }


    }
}


