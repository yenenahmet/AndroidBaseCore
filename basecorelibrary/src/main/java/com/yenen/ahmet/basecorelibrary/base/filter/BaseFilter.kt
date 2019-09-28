package com.yenen.ahmet.basecorelibrary.base.filter

import android.annotation.SuppressLint
import android.widget.Filter

abstract class BaseFilter<T> protected constructor(filterItems: List<T>) : Filter() {
    private var allItems: MutableList<T>? = null

    init {
        this.allItems = filterItems as MutableList<T>
    }

    @SuppressLint("DefaultLocale")
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        synchronized(this) {
            val results = FilterResults()
            if (constraint != null && constraint.length > 2) {
                val list: MutableList<T> = mutableListOf()
                val constLowerCase = constraint.toString().toLowerCase()
                val controlParameter = constLowerCase.substring(0, 2)
                val lowerCase = constLowerCase.substring(2, constLowerCase.length)
                for (values in allItems!!) {
                    values?.let {
                        getFilterItem(lowerCase, it, controlParameter)?.let { model ->
                            list.add(model)
                        }
                    }
                }
                results.values = list
                results.count = list.size
            } else {
                results.values = allItems
                results.count = allItems?.size!!
            }
            return results
        }
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults?) {
        synchronized(this) {
            results?.values?.let {
                val arrayList: List<*> = it as List<*>
                pubslishResults(arrayList)
            }
        }
    }


    protected abstract fun pubslishResults(results: List<*>)

    protected abstract fun getFilterItem(
        constLowerCase: String,
        value: T,
        controlParameter: String
    ): T?

    fun clear() {
        allItems?.clear()
    }


    @SuppressLint("DefaultLocale")
    fun isContainsLower(model: T, value: String, constLowerCase: String): T? {
        return if (value.toLowerCase().contains(constLowerCase)) {
            model
        } else null
    }


}