package com.test.medpersonal.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.test.medpersonal.domain.models.Users

class UserDiffUtil : DiffUtil.ItemCallback<Users>() {
    override fun areItemsTheSame(oldItem: Users, newItem: Users) =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: Users, newItem: Users) = oldItem == newItem

}

