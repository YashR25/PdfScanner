package com.example.simplepdfscanner.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplepdfscanner.databinding.PdfItemBinding
import com.example.simplepdfscanner.model.PdfModel

class PdfAdapter: ListAdapter<PdfModel,PdfAdapter.ViewHolder>(DiffCallBack) {

    lateinit var binding: PdfItemBinding

    inner class ViewHolder(binding: PdfItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = PdfItemBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = currentList[position]
        binding.pdfName.text = item.pdfName
        binding.pdfDesc.text = item.pdfDesc
    }

    object DiffCallBack:DiffUtil.ItemCallback<PdfModel>(){
        override fun areItemsTheSame(oldItem: PdfModel, newItem: PdfModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PdfModel, newItem: PdfModel): Boolean {
            return oldItem == newItem
        }

    }
}