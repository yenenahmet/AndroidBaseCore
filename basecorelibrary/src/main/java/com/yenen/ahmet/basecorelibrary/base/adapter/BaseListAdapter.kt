package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseListAdapter<T:Any, VB : ViewBinding>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, BaseListAdapter.ViewHolder>(diffUtil) {

    private fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = getViewBinding(getInflater(parent), parent)
        return ViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val binding = holder.itemBinding as VB
        binding.iniData(item)
    }

    override fun getItemCount(): Int = currentList.size
    class ViewHolder(
        val itemBinding: ViewBinding
    ) : RecyclerView.ViewHolder(itemBinding.root)

    fun setData(items: List<T>) {
        submitList(items)
    }

    fun setNewData(items:List<T>){
        submitList(items.toList())
    }

    protected abstract fun getViewBinding(
        layoutInflater: LayoutInflater,
        parent: ViewGroup
    ): ViewDataBinding

    protected abstract fun VB.iniData(item: T)

}