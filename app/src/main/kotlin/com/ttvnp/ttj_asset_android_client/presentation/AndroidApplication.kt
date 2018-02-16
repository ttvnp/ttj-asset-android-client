package com.ttvnp.ttj_asset_android_client.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Service
import android.content.Context
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.ttvnp.ttj_asset_android_client.presentation.di.component.DaggerApplicationComponent
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ApplicationModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject


open class AndroidApplication: MultiDexApplication(), HasActivityInjector, HasServiceInjector {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    @Inject
    lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector

    @Inject
    lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector

    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
        Fabric.with(this, Crashlytics())

        context = applicationContext
    }

}
