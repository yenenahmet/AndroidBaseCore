package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, E : RecyclerView.ViewHolder>
protected constructor(private var items: MutableList<T>) : RecyclerView.Adapter<E>() {


    override fun getItemCount(): Int {
        return items.size
    }

    // Set New Items -- Using Diff Util//
    fun setItems(newItems: List<T>) {
        val diff = BaseDiffUtil<T>(items, newItems)
        diff.calculateAndDispatch(this)
        this.items = newItems as MutableList<T>
    }


    // Set Fun //

    // GET FUN //
    fun getItems(): List<T> {
        return this.items
    }

    fun getItem(position: Int): T? {
        return if (position > -1 && items.isNotEmpty()) {
            items[position]
        } else null
    }

    fun getItemPosition(item: T): Int {
        if (items.isNotEmpty()) {
            var i = 0
            var pos = -1
            for (value in items) {
                if (item == value) {
                    pos = i
                    break
                }
                i++
            }
            return pos
        }
        return -1
    }

    // GET FUN //


    // Other Fun //
    fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    fun clearItems() {
        items.clear()
    }
// Other Fun //
}