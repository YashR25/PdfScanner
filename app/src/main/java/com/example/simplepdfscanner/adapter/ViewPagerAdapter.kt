package com.example.simplepdfscanner.adapter

import android.graphics.Bitmap
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplepdfscanner.databinding.ViewPagerItemBinding

class ViewPagerAdapter(private val list: List<Bitmap>): RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    lateinit var binding: ViewPagerItemBinding

    class ViewHolder(binding: ViewPagerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = ViewPagerItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bitmap = list[position]
        binding.imageItem.setImageBitmap(bitmap)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}