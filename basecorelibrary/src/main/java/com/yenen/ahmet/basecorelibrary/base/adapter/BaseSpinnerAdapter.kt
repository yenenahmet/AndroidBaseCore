package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.ViewDataBinding

abstract class BaseSpinnerAdapter<T,GVB:ViewDataBinding,GDDVB:ViewDataBinding>
constructor(private var items: MutableList<T>) : BaseAdapter() {

    fun setItems(items: List<T>) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        val binding = getViewBindingInflate(parent)
        if (view == null) {
            view = binding.root
        }
        onViewSetModel(binding,getItem(position))
        binding.executePendingBindings()
        return view
    }

    override fun getItem(position: Int): T {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        val binding = getDropDownViewBindingInflate(parent)
        if (view == null) {
            view = binding.root
        }
        onDropDownViewSetModel(binding,getItem(position))
        binding.executePendingBindings()
        return view
    }

    override fun getCount(): Int {
        return items.size
    }

    fun clear() {
        items.clear()
    }

    protected fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    protected abstract fun getViewBindingInflate(parent: ViewGroup?): GVB

    protected abstract fun onViewSetModel(binding: GVB,item :T)

    protected abstract fun getDropDownViewBindingInflate(parent: ViewGroup?): GDDVB

    protected abstract fun onDropDownViewSetModel(binding: GDDVB,item :T)

}