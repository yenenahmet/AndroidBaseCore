package com.yenen.ahmet.basecorelibrary.base.adapter

import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.yenen.ahmet.basecorelibrary.base.filter.BaseFilter
import java.util.*

/*
    Extends alınan adapter içinde Filterable implement edilmesinin tekrarının kalkması.
    Filter için class oluşturulması
    bağımlılığını kaldırmak ve Filtreleme işlemi için yazılan adapterların
    kod kalabalığını azaltmak için oluşturulmuştur.
 */
abstract class BaseRecyclerViewFilterAdapter<T, E : RecyclerView.ViewHolder> constructor(items: MutableList<T>,private val locale:Locale) :
    BaseRecyclerViewAdapter<T, E>(items), Filterable {

    private var filter: RecyclerViewFilter? = null

    // Filterable //
    override fun getFilter(): Filter {
        if (filter == null) {
            filter = RecyclerViewFilter(getItems(),locale)
        }
        return filter as RecyclerViewFilter
    }

    fun getFilterAdapter(): RecyclerViewFilter? {
        return filter
    }

    fun setFilter(filterValue: String) {
        getFilter().filter(filterValue)
    }

    inner class RecyclerViewFilter internal constructor(filterItems: List<T>,locale:Locale) :
        BaseFilter<T>(filterItems,locale) {

        override fun getFilterItem(constLowerCase: String, value: T, controlParameter: String): T? {
            if (filter != null) {
                return getRecyclerFilterItem(constLowerCase, value, controlParameter)
            }
            return null
        }

        @Suppress("UNCHECKED_CAST")
        override fun pubslishResults(results: List<*>) {
            if(!onFilterFinish(results as List<T>)){
                setItems(results as List<T>)
            }
        }
    }

    protected abstract fun getRecyclerFilterItem(
        constLowerCase: String,
        value: T,
        controlParameter: String
    ): T?
    // Filterable //

    // clear Memory //
    fun unBindFilterAdapter() {
        clearFilter()
        clearItems()
    }

    fun clearFilter() {
        filter?.clear()
        filter = null
    }
    // clear Memory //


    protected open fun onFilterFinish(results: List<T>):Boolean {

        return false
    }
}