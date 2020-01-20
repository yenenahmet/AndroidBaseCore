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

    // ADD  Fun//
    fun addItem(item: T) {
        items.add(item)
        val position = items.size - 1
        notifyItemInserted(position)
    }

    fun addItemNotNotify(item: T) {
        items.add(item)
    }

    fun addRestoreItem(item: T, pos: Int) {
        if (pos > -1) {
            items.add(pos, item)
            notifyItemInserted(pos)
        }
    }

    fun addItems(items: List<T>) {
        if (items.isNotEmpty()) {
            val x = items.size - 1
            this.items.addAll(items)
            val itemCount = items.size - 1
            notifyItemRangeInserted(x, itemCount)
        }
    }
    // ADD  Fun//

    //Remove Fun//
    fun removeItem(position: Int) {
        if (position > -1 && items.isNotEmpty()) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun removeItem(item: T) {
        val pos = getItemPosition(item)
        if (pos > -1) {
            items.remove(item)
            notifyItemRemoved(pos)
        }

    }
    //Remove Fun//

    // Other Fun //
    fun getInflater(parent: ViewGroup): LayoutInflater {
        return LayoutInflater.from(parent.context)
    }

    fun clearItems() {
        items.clear()
    }
// Other Fun //
}