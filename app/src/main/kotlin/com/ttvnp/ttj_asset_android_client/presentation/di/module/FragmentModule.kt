package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.*
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {

    // presenters
    @Provides
    fun mainHomePresenter(userUseCase: UserUseCase): MainHomePresenter {
        return MainHomePresenterImpl(userUseCase)
    }

    @Provides
    fun mainReceivePresenter(userUseCase: UserUseCase): MainReceivePresenter {
        return MainReceivePresenterImpl(userUseCase)
    }

    @Provides
    fun sendAmountFormPresenter(userUseCase: UserUseCase): SendAmountFormPresenter {
        return SendAmountFormPresenterImpl(userUseCase)
    }
}