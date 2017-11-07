package com.ttvnp.ttj_asset_android_client.presentation.ui.presenter

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import dagger.internal.Preconditions

abstract class BasePresenter {

    protected val disposables: CompositeDisposable = CompositeDisposable()

    /**
     * Dispose from current [CompositeDisposable].
     */
    fun dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose()
        }
    }

    /**
     * Dispose from current [CompositeDisposable].
     */
    protected fun addDisposable(disposable: Disposable) {
        Preconditions.checkNotNull(disposable)
        Preconditions.checkNotNull(disposables)
        disposables.add(disposable)
    }
}