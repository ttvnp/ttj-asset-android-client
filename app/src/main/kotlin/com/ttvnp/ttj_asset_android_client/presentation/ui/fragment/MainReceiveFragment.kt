package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.ReceiveSetAmountActivity

class MainReceiveFragment : BaseMainFragment() {

    private lateinit var textSetAmount: TextView

    companion object {
        fun getInstance() : MainReceiveFragment {
            return MainReceiveFragment()
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_receive, container, false)
        textSetAmount = view.findViewById<TextView>(R.id.text_set_amount)
        textSetAmount.setOnClickListener { view ->
            val intent = Intent(this.activity, ReceiveSetAmountActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}