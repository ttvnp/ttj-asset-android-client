package com.ttvnp.ttj_asset_android_client.presentation

import android.app.Activity
import android.support.multidex.MultiDexApplication
import android.app.Service
import com.squareup.leakcanary.LeakCanary
import com.ttvnp.ttj_asset_android_client.BuildConfig
import com.ttvnp.ttj_asset_android_client.presentation.di.component.DaggerApplicationComponent
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ApplicationModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


class AndroidApplication: MultiDexApplication(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()
        initializeLeakDetection()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
        Fabric.with(this, Crashlytics())
    }

    private fun initializeLeakDetection() {
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this)
        }
    }

}
