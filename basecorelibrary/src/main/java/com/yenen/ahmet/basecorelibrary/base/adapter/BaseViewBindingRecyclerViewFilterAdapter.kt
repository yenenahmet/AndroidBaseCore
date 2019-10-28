package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewBindingRecyclerViewFilterAdapter<T, VDB : ViewDataBinding> constructor(
    private val layoutRes: Int
) :
    BaseRecyclerViewFilterAdapter<T, BaseViewBindingRecyclerViewFilterAdapter<T, VDB>.ViewHolder>(
        mutableListOf()
    ) {

    private var listener: ClickListener<T>? = null

    interface ClickListener<T> {
        fun onItemClick(item: T, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<VDB>(getInflater(parent), layoutRes, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingRecyclerViewFilterAdapter<T, VDB>.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it)
            if (listener != null) {
                holder.binding.root.setOnClickListener { _ ->
                    listener?.onItemClick(it, position)
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T) {
            setBindingModel(item, binding as VDB)
            binding.executePendingBindings()
        }
    }

    abstract fun setBindingModel(item: T, binding: VDB)

    fun setListener(listener: ClickListener<T>) {
        this.listener = listener
    }

    fun unBind() {
        listener = null
    }
}