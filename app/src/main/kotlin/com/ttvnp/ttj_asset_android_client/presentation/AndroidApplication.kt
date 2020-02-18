package com.ttvnp.ttj_asset_android_client.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.multidex.MultiDexApplication
import com.ttvnp.ttj_asset_android_client.presentation.di.component.DaggerApplicationComponent
import com.ttvnp.ttj_asset_android_client.presentation.di.module.ApplicationModule
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


open class AndroidApplication: MultiDexApplication(), HasAndroidInjector {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    override fun androidInjector() = androidInjector


    override fun onCreate() {
        super.onCreate()
        DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
                .inject(this)
        context = applicationContext
    }

}
