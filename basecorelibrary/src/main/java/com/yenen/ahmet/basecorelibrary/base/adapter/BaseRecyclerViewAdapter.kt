package com.yenen.ahmet.basecorelibrary.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, E : RecyclerView.ViewHolder>
protected constructor(private var items: MutableList<T>) : RecyclerView.Adapter<E>() {


    override fun getItemCount(): Int {
        return items.size
    }

    // Set Fun //
    fun setItems(items: List<T>) {
        this.items = items as MutableList<T>
        notifyDataSetChanged()
    }

    fun setChanged(position: Int, item: T) {
        if (position > -1 && !this.items.isEmpty()) {
            this.items[position] = item
            notifyItemChanged(position, item)
        }
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

    protected fun getItemPosition(item: T): Int {
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

    // ADD  Fun//
    protected fun addItem(item: T) {
        items.add(item)
        val position = items.size - 1
        notifyItemInserted(position)
    }

    protected fun addItemNotNotify(item: T) {
        items.add(item)
    }

    protected fun addRestoreItem(item: T, pos: Int) {
        if (pos > -1) {
            items.add(pos, item)
            notifyItemInserted(pos)
        }
    }

    protected fun addItems(items: List<T>) {
        if (items.isNotEmpty()) {
            val x = items.size - 1
            this.items.addAll(items)
            val itemCount = items.size - 1
            notifyItemRangeInserted(x, itemCount)
        }
    }
    // ADD  Fun//

    //Remove Fun//
    protected fun removeItem(position: Int) {
        if (position > -1 && items.isNotEmpty()) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    protected fun removeItem(item: T) {
        val pos = getItemPosition(item)
        if (pos > -1) {
            items.remove(item)
            notifyItemRemoved(pos)
        }

    }
    //Remove Fun//

    // Other Fun //
    protected fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    fun clearItems() {
        items.clear()
    }
// Other Fun //
}