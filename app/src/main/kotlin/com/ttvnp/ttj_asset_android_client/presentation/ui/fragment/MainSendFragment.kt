package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.CaptureActivityAnyOrientation
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SendActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainSendPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainSendPresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MainSendFragment : BaseMainFragment(), MainSendPresenterTarget {

    @Inject
    lateinit var mainSendPresenter: MainSendPresenter

    private lateinit var textCannotSend: TextView
    private lateinit var buttonSendQR: Button
    private lateinit var buttonSendEmail: Button
    private lateinit var buttonSendStellar: Button

    private var isInit = false

    companion object {
        fun getInstance(): MainSendFragment {
            return MainSendFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mainSendPresenter.init(this)
        isInit = true
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main_send, container, false)

        textCannotSend = view.findViewById(R.id.text_can_not_send)
        buttonSendQR = view.findViewById(R.id.button_send_qr)
        buttonSendQR.setOnClickListener {
            val integrator = IntentIntegrator(activity)
            integrator.captureActivity = CaptureActivityAnyOrientation::class.java
            integrator.setOrientationLocked(false)
            integrator.initiateScan()
        }

        buttonSendEmail = view.findViewById(R.id.button_send_email)
        buttonSendEmail.setOnClickListener {
            SendActivity.start(context)
        }

        buttonSendStellar = view.findViewById(R.id.button_send_stellar)
        buttonSendStellar.setOnClickListener {
            SendActivity.start(context, true)
        }

        mainSendPresenter.setupDefault()
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && isInit) {
            mainSendPresenter.setupDefault()
        }
    }

    override fun setIdentify(identifier: Boolean) {
        if (identifier) {
            setEnableButton(true)
            return
        }

        setEnableButton(false)
    }

    @SuppressLint("ResourceAsColor")
    private fun setEnableButton(value: Boolean) {
        textCannotSend.visibility = View.INVISIBLE
        var textColor = R.color.colorTextOnPrimary

        if (!value) {
            textCannotSend.visibility = View.VISIBLE
            textColor = R.color.md_grey_500
        }

        buttonSendQR.isEnabled = value
        buttonSendQR.setTextColor(ContextCompat.getColor(buttonSendQR.context, textColor))
        buttonSendQR.visibility = View.VISIBLE

        buttonSendEmail.isEnabled = value
        buttonSendEmail.setTextColor(ContextCompat.getColor(buttonSendEmail.context, textColor))
        buttonSendEmail.visibility = View.VISIBLE

        buttonSendStellar.isEnabled = value
        buttonSendStellar.setTextColor(ContextCompat.getColor(buttonSendEmail.context, textColor))
        buttonSendStellar.visibility = View.VISIBLE
    }

}