package com.test.medpersonal.presentation.fragments.groupFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.medpersonal.databinding.ItemProfileBinding
import com.test.medpersonal.diffutil.OtherUserDiffUtil
import com.test.medpersonal.domain.models.OtherUserModel

class UserAdapter : ListAdapter<OtherUserModel, UserAdapter.UserViewHolder>(OtherUserDiffUtil()) {

    var onItemClick: ((OtherUserModel) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            ItemProfileBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    inner class UserViewHolder(private val binding: ItemProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(model: OtherUserModel?) {
            binding.tvUsername.text = model?.name
            binding.root.setOnClickListener{
                onItemClick?.invoke(getItem(absoluteAdapterPosition))
            }
        }
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

//    fun submitList(list: ArrayList<UserModel>) {
//
//    }

}