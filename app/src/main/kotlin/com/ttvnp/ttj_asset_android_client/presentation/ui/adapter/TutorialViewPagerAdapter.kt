package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class TutorialViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments: MutableList<Fragment> = mutableListOf()

    fun addFragment(fragment: Fragment) = fragments.add(fragment)

    override fun getCount(): Int = fragments.size

    override fun getItem(position: Int): Fragment? = fragments[position]
}
