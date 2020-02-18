package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.presentation.di.component.TutorialActivitySubcomponent
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.TutorialActivity
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjector
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module(subcomponents = [TutorialActivitySubcomponent::class])
abstract class TutorialActivityModule {
    @Binds
    @IntoMap
    @ClassKey(TutorialActivity::class)
    abstract fun bindTutorialActivityInjectorFactory(factory: TutorialActivitySubcomponent.Factory): AndroidInjector.Factory<*>
}
