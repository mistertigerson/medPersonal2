package com.test.medpersonal.presentation.fragments.homeFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.test.medpersonal.databinding.MainItemBinding
import com.test.medpersonal.diffutil.HomeDiffUtil
import com.test.medpersonal.domain.models.HomeModel
import com.test.medpersonal.extensions.loadImage

class HomeAdapter :
    ListAdapter<HomeModel, HomeAdapter.HomeViewHolder>(HomeDiffUtil()) {

    var onItemClick: ((HomeModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            MainItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    inner class HomeViewHolder(private val binding: MainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(homeModel: HomeModel) {
            binding.tvTitle.text = homeModel.title
            binding.image.loadImage(homeModel.image.toString())
            binding.tvDescription.text = homeModel.text
            binding.root.setOnClickListener {
                onItemClick?.invoke(getItem(absoluteAdapterPosition))
            }
        }


    }
}