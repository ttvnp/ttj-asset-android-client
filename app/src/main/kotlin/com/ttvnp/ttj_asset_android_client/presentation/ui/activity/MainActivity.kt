package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.widget.ImageView
import com.google.zxing.integration.android.IntentIntegrator
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
    private lateinit var receiveFragment: MainReceiveFragment

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
        adapter.addFragment(homeFragment)
        receiveFragment = MainReceiveFragment.getInstance()
        adapter.addFragment(receiveFragment)
        val sendFragment = MainSendFragment.getInstance()
        adapter.addFragment(sendFragment)
        val settingsFragment = MainSettingsFragment.getInstance()
        adapter.addFragment(settingsFragment)
        viewPager?.adapter = adapter
        tabLayout?.setupWithViewPager(viewPager)

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val homeTabView = inflater.inflate(R.layout.view_main_tab, null)
        tabLayout?.getTabAt(0)?.setCustomView(homeTabView)
        homeTabView.findViewById<ImageView>(R.id.image_tab).let {
            it.setImageResource(R.drawable.ic_home)
        }
        val receiveTabView = inflater.inflate(R.layout.view_main_tab, null)
        tabLayout?.getTabAt(1)?.setCustomView(receiveTabView)
        receiveTabView.findViewById<ImageView>(R.id.image_tab).let {
            it.setImageResource(R.drawable.ic_receive)
        }
        val sendTabView = inflater.inflate(R.layout.view_main_tab, null)
        tabLayout?.getTabAt(2)?.setCustomView(sendTabView)
        sendTabView.findViewById<ImageView>(R.id.image_tab).let {
            it.setImageResource(R.drawable.ic_send)
        }
        val settingsTabView = inflater.inflate(R.layout.view_main_tab, null)
        tabLayout?.getTabAt(3)?.setCustomView(settingsTabView)
        settingsTabView.findViewById<ImageView>(R.id.image_tab).let {
            it.setImageResource(R.drawable.ic_settings)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents != null) {
                val intent = Intent(this, SendActivity::class.java)
                intent.putExtra(SendActivity.INTENT_EXTRA_KEY, result.contents)
                startActivity(intent)
            }
        } else {
            when (requestCode) {
                MainReceiveFragment.SET_AMOUNT_ACTIVITY_REQUEST_CODE -> {
                    if (resultCode == Activity.RESULT_OK) {
                        data?.getStringExtra(ReceiveSetAmountActivity.INTENT_EXTRA_KEY)?.let {
                            receiveFragment.setQRCode(it)
                        }
                    }
                }
            }
        }
    }
}
