package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.google.zxing.qrcode.encoder.Encoder
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.ReceiveSetAmountActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainReceivePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainReceivePresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import android.graphics.Color
import android.widget.Button
import com.ttvnp.ttj_asset_android_client.presentation.ui.data.RequestCode

class MainReceiveFragment : BaseMainFragment(), MainReceivePresenterTarget {

    @Inject
    lateinit var mainReceivePresenter: MainReceivePresenter

    private lateinit var buttonSetAmount: Button
    private lateinit var imageQRCode: ImageView

    companion object {
        fun getInstance() : MainReceiveFragment {
            return MainReceiveFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mainReceivePresenter.init(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_receive, container, false)
        buttonSetAmount = view.findViewById<Button>(R.id.button_set_amount)
        buttonSetAmount.setOnClickListener {
            val intent = Intent(activity, ReceiveSetAmountActivity::class.java)
            activity.startActivityForResult(intent, RequestCode.SET_AMOUNT_ACTIVITY.rawValue)
        }
        imageQRCode = view.findViewById<ImageView>(R.id.image_qr_code)
        mainReceivePresenter.setupDefault()
        return view
    }

    override fun setQRCode(qrText: String) {
        val qrCode = Encoder.encode(qrText, ErrorCorrectionLevel.H)
        val byteMatrix = qrCode.matrix
        var bitmap = Bitmap.createBitmap(byteMatrix.width, byteMatrix.height, Bitmap.Config.ARGB_8888)
        for (y in 0 until byteMatrix.height) {
            for (x in 0 until byteMatrix.width) {
                val value = byteMatrix.get(x, y)
                bitmap.setPixel(x, y, if (value.toInt() == 1) Color.BLACK else Color.WHITE)
            }
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, imageQRCode.width, imageQRCode.height, false)
        imageQRCode.setImageBitmap(bitmap)
    }
}