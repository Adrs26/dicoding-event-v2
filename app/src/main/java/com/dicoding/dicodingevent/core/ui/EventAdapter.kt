package com.dicoding.dicodingevent.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.dicodingevent.core.domain.model.Event
import com.dicoding.dicodingevent.core.util.DateHelper.convertDate
import com.dicoding.dicodingevent.databinding.ItemEventBinding

class EventAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Event, EventAdapter.ItemViewHolder>(
    object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventBinding.inflate(inflater, parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewHolder(
        private val itemBinding: ItemEventBinding
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(data: Event) {
            Glide.with(itemBinding.root.context)
                .load(data.imageLogo)
                .centerCrop()
                .into(itemBinding.ivEventImage)

            itemBinding.tvEventTitle.text = data.name
            itemBinding.tvEventDate.text = convertDate(itemView.context, data.beginTime, data.endTime)
            itemBinding.tvEventCategory.text = data.category

            itemBinding.root.setOnClickListener {
                onItemClickListener.onItemClick(data.id)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }
}