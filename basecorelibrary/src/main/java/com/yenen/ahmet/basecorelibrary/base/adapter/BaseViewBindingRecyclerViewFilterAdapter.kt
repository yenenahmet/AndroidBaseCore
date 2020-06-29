package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@Suppress("UNCHECKED_CAST")
abstract class BaseViewBindingRecyclerViewFilterAdapter<T, VDB : ViewDataBinding> constructor(
    private val layoutRes: Int,locale:Locale
) :
    BaseRecyclerViewFilterAdapter<T, BaseViewBindingRecyclerViewFilterAdapter<T, VDB>.ViewHolder>(
        mutableListOf(),locale
    ) {

    private var listener: ClickListener<T,VDB>? = null
    private var filterListener: FilterListener<T>? = null
    private var longListener: LongClickListener<T, VDB>? =null

    interface ClickListener<T,VDB> {
        fun onItemClick(item: T, position: Int,binding:VDB)
    }

    interface FilterListener<T> {
        fun onFilterFinish(results: List<T>):Boolean
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
                    listener?.onItemClick(item, position,holder.binding as VDB)
                }
            }

            if(longListener!= null){
                holder.binding.root.setOnLongClickListener {
                    longListener?.onItemLongClick(item, position,holder.binding as VDB)
                    true
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

    fun setListener(listener: ClickListener<T,VDB>) {
        this.listener = null
        this.listener = listener
    }

    fun unBind() {
        listener = null
        filterListener = null
        longListener = null
    }

    fun setLongClickListener(listener: LongClickListener<T, VDB>){
        this.longListener = null
        this.longListener = listener
    }

    fun setFilterListener(listener: FilterListener<T>) {
        this.filterListener = null
        this.filterListener = listener
    }

    override fun onFilterFinish(results: List<T>) :Boolean{
        return filterListener?.onFilterFinish(results)?:false
    }
}