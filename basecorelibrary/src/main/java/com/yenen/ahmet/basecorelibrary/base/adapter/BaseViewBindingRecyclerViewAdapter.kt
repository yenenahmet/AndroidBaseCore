package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.yenen.ahmet.basecorelibrary.base.extension.setSafeOnClickListener

abstract class BaseViewBindingRecyclerViewAdapter<T, VDB : ViewDataBinding>
    (
    @LayoutRes private val layoutRes: Int
) :
    BaseRecyclerViewAdapter<T, BaseViewBindingRecyclerViewAdapter<T, VDB>.ViewHolder>(mutableListOf()) {

    private var listener: ClickListener<T,VDB>? = null
    private var longListener:LongClickListener<T,VDB>? =null

    interface ClickListener<T,VDB> {
        fun onItemClick(item: T, position: Int,rowBinding: VDB)
    }

    interface LongClickListener<T,VDB>{
        fun onItemLongClick(item: T, position: Int,rowBinding: VDB)
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
        getItem(position)?.let {item->
            holder.bind(item,position)
            if (listener != null) {
                holder.binding.root.setSafeOnClickListener { _ ->
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
        fun bind(item: T,position: Int) {
            setBindingModel(item, binding as VDB,position)
            binding.executePendingBindings()
        }
    }


    fun setListener(listener: ClickListener<T,VDB>) {
        this.listener = null
        this.listener = listener
    }

    fun setLongClickListener(listener:LongClickListener<T,VDB>){
        this.longListener = null
        this.longListener = listener
    }

    open fun unBind() {
        this.listener = null
        this.longListener =null
    }

    protected abstract fun setBindingModel(item: T, binding: VDB,position: Int)

}