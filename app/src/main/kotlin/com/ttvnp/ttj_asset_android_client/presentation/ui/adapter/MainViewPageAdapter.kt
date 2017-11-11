package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.graphics.drawable.Drawable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.BaseMainFragment
import android.text.Spannable
import android.text.style.ImageSpan
import android.text.SpannableString

class MainViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: MutableList<BaseMainFragment> = mutableListOf()
    private val iconImages: MutableList<Drawable> = mutableListOf()

    fun addFragment(fragment: BaseMainFragment, image: Drawable) {
        fragments.add(fragment)
        iconImages.add(image)
    }

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment? = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        val image = iconImages[position]
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight())
        // Replace blank spaces with image icon
        val sb = SpannableString(" ")
        val imageSpan = ImageSpan(image, ImageSpan.ALIGN_BOTTOM)
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return sb
    }
}
