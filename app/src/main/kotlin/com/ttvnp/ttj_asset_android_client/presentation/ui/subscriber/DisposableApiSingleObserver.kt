package com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber

import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.MaintenanceActivity
import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException

abstract class DisposableApiSingleObserver<T> : DisposableSingleObserver<T>() {

    override fun onError(error: Throwable?) {
        error?.printStackTrace()
        val httpException = error as? HttpException
        val statusCode = httpException?.code() ?: 0
        val handle = when (statusCode) {
            503 -> {
                MaintenanceActivity.start()
                return
            }
            else -> {
                error
            }
        }
        onOtherError(handle)
    }

    abstract fun onOtherError(error: Throwable?)

}