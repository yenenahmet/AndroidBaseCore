package com.yenen.ahmet.basecorelibrary.base.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener constructor(private val layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {


    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val isTotalItemCount = (visibleItemCount + firstVisibleItemPosition) >= totalItemCount

        if (!isLoading() && !isLastPage() && isTotalItemCount && firstVisibleItemPosition >= 0) {
            loadMoreItems()
        }

    }

    protected abstract fun loadMoreItems()

    abstract fun isLastPage(): Boolean

    abstract fun isLoading(): Boolean
}