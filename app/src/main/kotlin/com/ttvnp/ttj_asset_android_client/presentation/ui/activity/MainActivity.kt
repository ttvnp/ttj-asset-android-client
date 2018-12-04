package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.crash.FirebaseCrash
import com.google.zxing.integration.android.IntentIntegrator
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.MainViewPageAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.PushNotificationBridgeData
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainHomeFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainReceiveFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSendFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.MainSettingsFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.DeviceTokenUpdater
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    @Inject
    lateinit var deviceTokenUpdater : DeviceTokenUpdater

    private var tabLayout: TabLayout? = null
    private var viewPager: ViewPager? = null
    lateinit var adapter: MainViewPageAdapter
    private lateinit var homeFragment: MainHomeFragment
    private lateinit var receiveFragment: MainReceiveFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.tab_layout_main)
        viewPager = findViewById(R.id.view_pager_main)

        // set up view pager
        val fragmentManager = supportFragmentManager
        adapter = MainViewPageAdapter(fragmentManager)
        homeFragment = MainHomeFragment.getInstance()
        adapter.addFragment(homeFragment)
        receiveFragment = MainReceiveFragment.getInstance()
        adapter.addFragment(receiveFragment)
        val sendFragment = MainSendFragment.getInstance()
        adapter.addFragment(sendFragment)
        val settingsFragment = MainSettingsFragment.getInstance()
        adapter.addFragment(settingsFragment)
        viewPager?.adapter = adapter
        viewPager?.offscreenPageLimit = adapter.count
        tabLayout?.setupWithViewPager(viewPager)

        val homeTabView = View.inflate(this, R.layout.view_main_tab, null)
        tabLayout?.getTabAt(0)?.customView = homeTabView
        homeTabView.findViewById<ImageView>(R.id.image_tab).setImageResource(R.drawable.ic_home)
        val receiveTabView = View.inflate(this, R.layout.view_main_tab, null)
        tabLayout?.getTabAt(1)?.customView = receiveTabView
        receiveTabView.findViewById<ImageView>(R.id.image_tab).setImageResource(R.drawable.ic_receive)
        val sendTabView = View.inflate(this, R.layout.view_main_tab, null)
        tabLayout?.getTabAt(2)?.customView = sendTabView
        sendTabView.findViewById<ImageView>(R.id.image_tab).setImageResource(R.drawable.ic_send)
        val settingsTabView = View.inflate(this, R.layout.view_main_tab, null)
        tabLayout?.getTabAt(3)?.customView = settingsTabView
        settingsTabView.findViewById<ImageView>(R.id.image_tab).setImageResource(R.drawable.ic_settings)

        handleNotified()
        deviceTokenUpdater.updateDeviceTokenIfEmpty()
    }

    override fun onNewIntent(intent: Intent?) {
        handleNotified()
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
                RequestCode.SET_AMOUNT_ACTIVITY.rawValue -> {
                    if (resultCode == Activity.RESULT_OK) {
                        data?.getStringExtra(ReceiveSetAmountActivity.INTENT_EXTRA_KEY)?.let {
                            receiveFragment.setQRCode(it)
                        }
                    }
                }
            }
        }
    }

    private fun handleNotified() {
        val data = intent.getSerializableExtra(PushNotificationBridgeData.INTENT_KEY)
        if (data == null || (data !is PushNotificationBridgeData)) {
            return
        }
        try {
            val messageId = resources.getIdentifier(data.messageKey, "string", packageName)
            val message = getString(messageId, *data.getMessageArgs())
            // go to home fragment.
            homeFragment.mainHomePresenter.setupUserTransactions(true)
            viewPager?.setCurrentItem(0, true)
            Toast.makeText(
                    this,
                    message,
                    Toast.LENGTH_LONG
            ).show()
        } catch (e: Throwable) {
            FirebaseCrash.report(e)
        }
    }
}
