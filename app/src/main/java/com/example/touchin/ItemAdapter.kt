package com.example.touchin

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.touchin.databinding.ItemLayoutBinding


class ItemAdapter(val context: Context, val items: List<ItemModel>) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.binding.nameTv.text = item.name
        holder.binding.locationTv.text = item.location
        holder.binding.organisationTv.text = item.organisation
    }

    inner class ItemViewHolder(val binding: ItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)
}