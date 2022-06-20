package com.test.medpersonal.presentation.fragments.chatFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.test.medpersonal.databinding.UserListItemBinding
import com.test.medpersonal.diffutil.MessageDiffUtil

import com.test.medpersonal.domain.models.Post
import com.test.medpersonal.domain.models.Users

class ChatAdapter : ListAdapter<Post, ChatAdapter.MessageViewHolder>(MessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            UserListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    inner class MessageViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Post) {
            val user = FirebaseAuth.getInstance()
            val userReference = Firebase.database.reference
                .child("users").child(item.author.toString())
            val userListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    val users_Model = dataSnapshot.getValue(Users::class.java)
                    //binding.ivAvatar.source = user_Model?.image
                    //  val i = users_Model?.name
                    //     Log.d(TAG, i.toString())
                    binding.tvUser.text = users_Model?.name

                }

                override fun onCancelled(error: DatabaseError) {


                }
            }
            userReference.addListenerForSingleValueEvent(userListener)
            binding.tvName.text = item.date
            binding.tvMessage.text = item.body


        }

    }
}