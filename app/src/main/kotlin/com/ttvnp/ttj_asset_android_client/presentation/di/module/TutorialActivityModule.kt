package com.ttvnp.ttj_asset_android_client.presentation.di.module

import android.app.Activity
import com.ttvnp.ttj_asset_android_client.presentation.di.component.TutorialActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.TutorialActivity
import dagger.Binds
import dagger.Module
import dagger.android.ActivityKey
import dagger.android.AndroidInjector
import dagger.multibindings.IntoMap

@Module(subcomponents = arrayOf(TutorialActivitySubcomponent::class))
abstract class TutorialActivityModule {
    @Binds
    @IntoMap
    @ActivityKey(TutorialActivity::class)
    abstract fun bindTutorialActivityInjectorFactory(builder: TutorialActivitySubcomponent.Builder): AndroidInjector.Factory<out Activity>
}