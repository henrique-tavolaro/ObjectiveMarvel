package com.example.marvelproject.presenter

import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.marvelproject.databinding.AdapterBinding
import com.example.marvelproject.domain.model.Result

class CharacterAdapter(val clickListener: CharacterListener) : ListAdapter<Result, CharacterAdapter.ItemViewHolder>(LazyCallback()) {

    class ItemViewHolder private constructor (val binding: AdapterBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(
            item: Result,
            clickListener: CharacterListener
        ) {
            binding.dataResponse = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val binding = AdapterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ItemViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }
}

class LazyCallback : DiffUtil.ItemCallback<Result>(){
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }
}

class CharacterListener(val clickListener: (result: Result) -> Unit){
    fun onClick(result: Result) = clickListener(result)
}