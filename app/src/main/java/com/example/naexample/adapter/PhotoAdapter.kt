package com.example.naexample.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.naexample.R
import com.example.naexample.databinding.PhotoItemBinding
import com.example.naexample.model.Photos
import com.squareup.picasso.Picasso

class PhotoAdapter :
    PagingDataAdapter<Photos, PhotoAdapter.PhotoVH>(diffCallback) {

    private var onClick: ((item: Photos) -> Unit?)? = null

    fun setOnClickListener(listener: (item: Photos) -> Unit) {
        onClick = listener
    }

    inner class PhotoVH(private val itemBinding: PhotoItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(item: Photos) = with(itemBinding) {

            item.src?.large.let {
                try {
                    Picasso.get().load(it).placeholder(R.drawable.image_preview).into(photo)
                } catch (_: Exception) {
                }
            }
            photographer.text = item.photographer
            itemBinding.root.setOnClickListener { onClick?.invoke(item) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<Photos>() {
            override fun areItemsTheSame(
                oldItem: Photos, newItem: Photos
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Photos, newItem: Photos
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoVH {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = PhotoItemBinding.inflate(layoutInflater, parent, false)
        return PhotoVH(binding)
    }

    override fun onBindViewHolder(holder: PhotoVH, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }
}
