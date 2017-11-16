package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileDetailFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SettingsProfileActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_settings_profile)
        toolbar.title = getString(R.string.title_settings_profile)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        val cancel: () -> Unit = {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent);
            finish()
        }

        toolbar.setNavigationOnClickListener {
            cancel()
        }

        if (savedInstanceState != null) return

        val detailFragment = SettingsProfileDetailFragment.getInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.settings_profile_activity_fragment_container, detailFragment)
                .commit()
    }
}
