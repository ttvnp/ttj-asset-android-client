package com.ttvnp.ttj_asset_android_client.presentation.ui.view

import android.content.Context
import androidx.viewpager.widget.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class ScrollControllViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {

    constructor(context: Context) : this(context, null)

    companion object {
        val SCROLL_NONE = 0b00 // None
        val SCROLL_PREV = 0b01 // Left
        val SCROLL_NEXT = 0b10 // Right
        val SCROLL_BOTH = SCROLL_NEXT + SCROLL_PREV
    }

    var scrollDirection = SCROLL_BOTH

    private var downX: Float? = null

    override fun onTouchEvent(ev: MotionEvent): Boolean =
            if (canScroll(ev)) super.onTouchEvent(ev) else false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean =
            if (canScroll(ev)) super.onInterceptTouchEvent(ev) else false

    private fun canScroll(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            downX = ev.x
            return true
        }
        downX?.let {
            val diffX = ev.x - it
            if (diffX > 0 && scrollDirection.and(SCROLL_PREV) == 0) {
                return false
            }
            if (diffX < 0 && scrollDirection.and(SCROLL_NEXT) == 0) {
                return false
            }
        }
        return true
    }
}