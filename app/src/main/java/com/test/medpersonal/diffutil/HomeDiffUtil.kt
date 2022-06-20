package com.test.medpersonal.diffutil

import androidx.recyclerview.widget.DiffUtil
import com.test.medpersonal.domain.models.HomeModel

class HomeDiffUtil : DiffUtil.ItemCallback<HomeModel>() {
    override fun areItemsTheSame(oldItem: HomeModel, newItem: HomeModel) =
        oldItem.title == newItem.title


    override fun areContentsTheSame(oldItem: HomeModel, newItem: HomeModel) =
        oldItem == newItem
}