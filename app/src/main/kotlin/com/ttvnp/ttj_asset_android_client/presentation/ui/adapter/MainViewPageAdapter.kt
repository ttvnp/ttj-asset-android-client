package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.BaseMainFragment

class MainViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: MutableList<BaseMainFragment> = mutableListOf()

    fun addFragment(fragment: BaseMainFragment) {
        fragments.add(fragment)
    }

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment? = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}
