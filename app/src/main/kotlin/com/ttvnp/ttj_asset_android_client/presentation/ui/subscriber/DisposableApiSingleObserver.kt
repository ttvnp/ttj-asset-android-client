package com.ttvnp.ttj_asset_android_client.presentation.ui.subscriber

import io.reactivex.observers.DisposableSingleObserver
import retrofit2.HttpException

abstract class DisposableApiSingleObserver<T> : DisposableSingleObserver<T>() {

    override fun onError(e: Throwable) {
        e.printStackTrace()
        val httpException = e as? HttpException
        val statusCode = httpException?.code() ?: 0
        val handle = when (statusCode) {
            503 -> {
                onMaintenance()
                return
            }
            else -> {
                e
            }
        }
        onOtherError(handle)
    }

    abstract fun onOtherError(error: Throwable?)

    abstract fun onMaintenance()

}