package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.viewpager.widget.PagerAdapter

abstract class BaseViewBindingPagerAdapter<VDB : ViewDataBinding, T>(
    @LayoutRes private val layoutRes: Int, private val items: MutableList<T>,
    private var listener: Listener<VDB, T>?
) : PagerAdapter() {

    interface Listener<VDB : ViewDataBinding, T> {
        fun setBindingModel(binding: VDB, item: T)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutRes, container, false) as VDB
        container.addView(binding.root)
        listener?.setBindingModel(binding, items[position])
        binding.executePendingBindings()
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }


    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getCount(): Int = items.size

    fun getItem(position:Int) :T{
        return items[position]
    }

    fun clear(){
        items.clear()
    }

    fun unBind() {
        listener = null
    }
}