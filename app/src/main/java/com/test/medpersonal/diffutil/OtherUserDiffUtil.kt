package com.test.medpersonal.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.test.medpersonal.domain.models.OtherUserModel


class OtherUserDiffUtil : DiffUtil.ItemCallback<OtherUserModel>() {
    override fun areItemsTheSame(oldItem: OtherUserModel, newItem: OtherUserModel) =
        oldItem.uid == newItem.uid

    override fun areContentsTheSame(oldItem: OtherUserModel, newItem: OtherUserModel) = oldItem == newItem

}

