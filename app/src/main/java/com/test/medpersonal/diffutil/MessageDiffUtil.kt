package com.test.medpersonal.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.test.medpersonal.domain.models.HomeModel

import com.test.medpersonal.domain.models.Post

class MessageDiffUtil: DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post) =
        oldItem.author == newItem.author


    override fun areContentsTheSame(oldItem: Post, newItem: Post) =
        oldItem == newItem
}