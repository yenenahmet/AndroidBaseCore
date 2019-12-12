package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseSpinnerAdapter<T, GVB : ViewDataBinding, GDDVB : ViewDataBinding>
constructor(
    private var items: MutableList<T>,
    @LayoutRes private val viewLayoutRes: Int,
    @LayoutRes private val dropDownLayoutRes:Int
) :
    BaseAdapter() {


    fun setItems(items: List<T>) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    @Suppress("UNCHECKED_CAST")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        val binding:GVB
        if(view==null){
            binding = DataBindingUtil.inflate(
                getInflater(parent!!),
                viewLayoutRes,
                parent,
                false
            )
            view = binding.root
            view.tag = binding
        }else{
            binding =  view.tag as GVB
        }

        onViewSetModel(binding, getItem(position))
        binding.executePendingBindings()
        return view
    }


    @Suppress("UNCHECKED_CAST")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View? = convertView
        val binding:GDDVB
        if (view == null) {
             binding = DataBindingUtil.inflate(
                getInflater(parent!!),
                dropDownLayoutRes,
                parent,
                false
            )
            view = binding.root
            view.tag = binding
        } else {
            binding =  view.tag as GDDVB
        }

        onDropDownViewSetModel(binding, getItem(position))
        binding.executePendingBindings()
        return view
    }

    override fun getItem(position: Int): T {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
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


    protected abstract fun onViewSetModel(binding: GVB, item: T)

    protected abstract fun onDropDownViewSetModel(binding: GDDVB, item: T)

}