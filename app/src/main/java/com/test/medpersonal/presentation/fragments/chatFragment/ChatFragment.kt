package com.test.medpersonal.presentation.fragments.chatFragment

import android.content.ContentValues.TAG
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.R
import com.test.medpersonal.databinding.FragmentChatBinding
import com.test.medpersonal.domain.models.Post
import com.test.medpersonal.domain.models.Users
import java.time.LocalDateTime


class ChatFragment : Fragment(R.layout.fragment_chat) {

    private val binding: FragmentChatBinding by viewBinding()
    private val args: ChatFragmentArgs by navArgs()
    private val adapter: ChatAdapter by lazy {
        ChatAdapter()
    }
    private var list: ArrayList<Post> = arrayListOf()
    private lateinit var listData: Post
    lateinit var auth: FirebaseAuth
    lateinit var model: Users
    lateinit var modelPost: Post
    private lateinit var postReference: DatabaseReference
    private lateinit var database: DatabaseReference

    private var postListener: ValueEventListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        auth = Firebase.auth
        database = Firebase.database.reference
        postReference =
            Firebase.database.reference.child("soobshenie").child(auth.currentUser?.uid.toString())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        val user = FirebaseAuth.getInstance().currentUser
        Log.e("TAG", "onViewCreated: ${user?.uid}")


        val postListener = object : ValueEventListener {

            override fun onDataChange(
                dataSnapshot: DataSnapshot
            ) {
                list.clear()


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                Toast.makeText(
                    context, "Failed to load post.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // postReference.addValueEventListener(postListener)

        // Keep copy of post listener so we can remove it when app stops
        // this.postListener = postListener

        binding.btnSend.setOnClickListener {
            postReference = Firebase.database.reference.child("soobshenie")
                .child(auth.currentUser?.uid.toString())
            //------------
            val key = Firebase.database.reference.child("soobshenie")
                .child(auth.currentUser?.uid.toString()).push().key
            if (key == null) {
                Log.w(TAG, "Couldn't get push key for posts")
                // return
            }


            modelPost = Post(
                key,
                auth.currentUser?.uid.toString(),
                args.to,
                binding.etText.text.toString(),
                LocalDateTime.now().toString()
            )


            val profileValues = modelPost.toMap()
            val userId = auth.currentUser?.uid.toString()
            val childUpdates = hashMapOf<String, Any>(
                "/$key" to profileValues
                //"/soobshenie/$userId/$key" to profileValues
            )

            postReference.updateChildren(childUpdates)
            // -------------
//          /  postReference.child()
//                .setValue(Post(author = binding.etText.toString()))
//            Log.e("TAG", "onViewCreated: ${user?.displayName.toString()}")
            binding.etText.text.clear()
        }
        onChangeListener(postReference)
    }

    private fun onChangeListener(dRef: DatabaseReference) {
        postReference = Firebase.database.reference.child("soobshenie")
        postReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (s in snapshot.children) {
                    for (m in s.children) {
                        val post = m.getValue(Post::class.java)
                        if (post != null) {
                            if ((post.author == args.to) && (post.to == auth.currentUser?.uid.toString())
                                || ((post.author == auth.currentUser?.uid.toString()) && (post.to == args.to))
                            )
                                list.add(post)
                        }
                    }
                    adapter.submitList(list)
                    adapter.notifyDataSetChanged()
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }


    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity(),
                OnCompleteListener<AuthResult?> { task ->
                    if (!task.isSuccessful) {
                        Toast.makeText(
                            requireContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        acct.displayName.toString()
                        println(acct.photoUrl)
                    }
                })
    }

    private fun initRv() {
        binding.rvMessage.apply {
            adapter = this@ChatFragment.adapter
            layoutManager = LinearLayoutManager(context)
        }
    }

}

