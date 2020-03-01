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
    private var filterListener: FilterListener<T>? = null
    private var longListener: LongClickListener<T, VDB>? =null

    interface ClickListener<T> {
        fun onItemClick(item: T, position: Int)
    }

    interface FilterListener<T> {
        fun onFilterFinish(results: List<T>)
    }

    interface LongClickListener<T,VDB>{
        fun onItemLongClick(item: T, position: Int,rowBinding: VDB)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<VDB>(getInflater(parent), layoutRes, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BaseViewBindingRecyclerViewFilterAdapter<T, VDB>.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {item->
            holder.bind(item)
            if (listener != null) {
                holder.binding.root.setOnClickListener { _ ->
                    listener?.onItemClick(item, position)
                }
            }

            if(longListener!= null){
                holder.binding.root.setOnLongClickListener {
                    longListener?.onItemLongClick(item, position,holder.binding as VDB)
                    false
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
        filterListener = null
    }

    fun setLongClickListener(listener: LongClickListener<T, VDB>){
        this.longListener = listener
    }

    fun setFilterListener(listener: FilterListener<T>) {
        this.filterListener = listener
    }

    override fun onFilterFinish(results: List<T>) {
        filterListener?.onFilterFinish(results)
    }
}