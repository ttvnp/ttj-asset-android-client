package com.ttvnp.ttj_asset_android_client.presentation

import android.app.Activity
import android.app.Application
import com.squareup.leakcanary.LeakCanary
import com.ttvnp.ttj_asset_android_client.presentation.BuildConfig
import com.ttvnp.ttj_asset_android_client.presentation.di.component.DaggerApplicationComponent
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ApplicationModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class AndroidApplication: Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        initializeLeakDetection()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
    }

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
        }
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }
}
