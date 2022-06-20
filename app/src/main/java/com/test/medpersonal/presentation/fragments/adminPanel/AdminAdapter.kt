package com.test.medpersonal.presentation.fragments.adminPanel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.medpersonal.databinding.AdminItemBinding
import com.test.medpersonal.domain.models.AdminModel

class AdminAdapter : ListAdapter<AdminModel, AdminAdapter.AdminViewHolder>(AdminDiffUtil()) {

    var onItemClick: ((AdminModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminViewHolder {
        return AdminViewHolder(
            AdminItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    inner class AdminViewHolder(private val binding: AdminItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: AdminModel) {
            binding.tvTitle.text = item.title
            binding.tvDescription.text = item.title
            binding.root.setOnClickListener {
                onItemClick?.invoke(getItem(absoluteAdapterPosition))
            }
        }

    }

    override fun onBindViewHolder(holder: AdminViewHolder, position: Int) {
        holder.onBind(getItem(position))

    }
}