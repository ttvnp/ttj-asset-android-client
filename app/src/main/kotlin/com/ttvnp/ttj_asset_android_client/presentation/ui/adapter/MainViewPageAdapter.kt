package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.BaseMainFragment

class MainViewPageAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val fragments: MutableList<BaseMainFragment> = mutableListOf()

    fun addFragment(fragment: BaseMainFragment) {
        fragments.add(fragment)
    }

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getPageTitle(position: Int): CharSequence? {
        return null
    }
}
