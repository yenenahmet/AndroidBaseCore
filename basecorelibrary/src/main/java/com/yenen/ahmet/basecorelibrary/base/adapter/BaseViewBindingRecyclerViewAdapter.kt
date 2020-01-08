package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewBindingRecyclerViewAdapter<T, VDB : ViewDataBinding>
    (
    @LayoutRes private val layoutRes: Int
) :
    BaseRecyclerViewAdapter<T, BaseViewBindingRecyclerViewAdapter<T, VDB>.ViewHolder>(mutableListOf()) {

    private var listener: ClickListener<T,VDB>? = null

    interface ClickListener<T,VDB> {
        fun onItemClick(item: T, position: Int,rowBinding: VDB)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<VDB>(getInflater(parent), layoutRes, parent, false)
        return ViewHolder(binding)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: BaseViewBindingRecyclerViewAdapter<T, VDB>.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it,position)
            if (listener != null) {
                holder.binding.root.setOnClickListener { _ ->
                    listener?.onItemClick(it, position,holder.binding as VDB)
                }
            }
        }
    }


    @Suppress("UNCHECKED_CAST")
    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T,position: Int) {
            setBindingModel(item, binding as VDB,position)
            binding.executePendingBindings()
        }
    }


    fun setListener(listener: ClickListener<T,VDB>) {
        this.listener = listener
    }

    open fun unBind() {
        listener = null
    }

    protected abstract fun setBindingModel(item: T, binding: VDB,position: Int)

}