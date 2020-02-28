package com.ttvnp.ttj_asset_android_client.presentation.ui.listener

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    var firstVisibleItem: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0
    private var previousTotal: Int = 0
    private var loading: Boolean = true
    private var currentPage: Int = 0
    private var finished: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (finished) return

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleItemCount)) {
            currentPage++
            loading = true
            onLoadMore(currentPage)
        }
    }

    fun setFinished() {
        finished = true
    }

    abstract fun onLoadMore(currentPage: Int)
}
