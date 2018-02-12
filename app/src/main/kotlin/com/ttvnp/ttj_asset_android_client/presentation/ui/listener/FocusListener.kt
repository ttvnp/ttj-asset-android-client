package com.ttvnp.ttj_asset_android_client.presentation.ui.listener

import android.view.View
import android.widget.EditText

fun getOnFocusChangeListener(hint: String): View.OnFocusChangeListener {

    return View.OnFocusChangeListener { view, hasFocus ->
        if (view !is EditText) return@OnFocusChangeListener
        if (hasFocus) {
            view.hint = hint
            return@OnFocusChangeListener
        }
        view.hint = ""
    }

}