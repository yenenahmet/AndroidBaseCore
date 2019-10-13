package com.yenen.ahmet.basecorelibrary.base.binding

import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

object AdapterBindings {

    @BindingAdapter("setAdapter")
    @JvmStatic
    fun bindRecyclerViewAdapter(view: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        view.setHasFixedSize(true)
        view.itemAnimator = DefaultItemAnimator()
        view.adapter = adapter
    }

    @BindingAdapter("setSpinnerAdapter")
    @JvmStatic
    fun bindSpinnerViewAdapter(view: AppCompatSpinner, adapter: BaseAdapter) {
        view.adapter = adapter
    }

}