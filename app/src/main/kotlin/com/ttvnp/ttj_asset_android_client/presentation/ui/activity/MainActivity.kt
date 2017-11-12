package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.MainViewPageAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainHomeFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainReceiveFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSendFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSettingsFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById<TabLayout>(R.id.tab_layout_main)
        viewPager = findViewById<ViewPager>(R.id.view_pager_main)

        // set up view pager
        val fragmentManager = getSupportFragmentManager()
        val adapter = MainViewPageAdapter(fragmentManager)
        val homeFragment = MainHomeFragment.getInstance()
        adapter.addFragment(homeFragment, resources.getDrawable(R.drawable.ic_home))
        val receiveFragment = MainReceiveFragment.getInstance()
        adapter.addFragment(receiveFragment, resources.getDrawable(R.drawable.ic_receive))
        val sendFragment = MainSendFragment.getInstance()
        adapter.addFragment(sendFragment, resources.getDrawable(R.drawable.ic_send))
        val settingsFragment = MainSettingsFragment.getInstance()
        adapter.addFragment(settingsFragment, resources.getDrawable(R.drawable.ic_settings))
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)

        // viewPager?.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        // tabLayout?.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
    }
}
