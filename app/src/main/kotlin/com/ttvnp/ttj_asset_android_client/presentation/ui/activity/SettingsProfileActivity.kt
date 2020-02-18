package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.Toolbar
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SettingsProfileDetailFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SettingsProfileActivity : BaseActivity(), HasAndroidInjector {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SettingsProfileActivity::class.java)
            context?.startActivity(intent)
        }
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    lateinit var toolbar: Toolbar
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_profile)

        toolbar = findViewById(R.id.toolbar_settings_profile)

        if (savedInstanceState != null) return
        val detailFragment = SettingsProfileDetailFragment.getInstance()
        supportFragmentManager
                .beginTransaction()
                .add(R.id.settings_profile_activity_fragment_container, detailFragment)
                .commit()
    }
}
