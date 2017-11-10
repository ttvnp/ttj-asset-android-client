package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ttvnp.ttj_asset_android_client.presentation.R

class MainSendFragment : BaseMainFragment() {

    companion object {
        fun getInstance() : MainSendFragment {
            return MainSendFragment()
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_send, container, false)
        return view
    }
}