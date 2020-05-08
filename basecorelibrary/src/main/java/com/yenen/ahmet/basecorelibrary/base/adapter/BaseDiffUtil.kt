package com.yenen.ahmet.basecorelibrary.base.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class BaseDiffUtil<T>(
    private val oldList:List<T>,
    private val newList:List<T>
) :DiffUtil.Callback(){


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
      return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    fun calculateAndDispatch(adapter: RecyclerView.Adapter<*>) {
        DiffUtil.calculateDiff(this).dispatchUpdatesTo(adapter)
    }
}