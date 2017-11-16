package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.zxing.integration.android.IntentIntegrator
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.CaptureActivityAnyOrientation
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SendActivity

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

        val buttonSendQR = view.findViewById<Button>(R.id.button_send_qr)
        buttonSendQR.setOnClickListener {
            val integrator = IntentIntegrator(activity)
            integrator.setCaptureActivity(CaptureActivityAnyOrientation::class.java)
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        val buttonSendEmail = view.findViewById<Button>(R.id.button_send_email)
        buttonSendEmail.setOnClickListener {
            val intent = Intent(activity, SendActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}