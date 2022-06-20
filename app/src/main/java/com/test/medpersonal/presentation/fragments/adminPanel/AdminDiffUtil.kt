package com.test.medpersonal.presentation.fragments.adminPanel

import androidx.recyclerview.widget.DiffUtil
import com.test.medpersonal.domain.models.AdminModel

class AdminDiffUtil: DiffUtil.ItemCallback<AdminModel>() {
    override fun areItemsTheSame(oldItem: AdminModel, newItem: AdminModel) =
        oldItem.title == newItem.title


    override fun areContentsTheSame(oldItem: AdminModel, newItem: AdminModel) =
        oldItem == newItem

}