package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.WebView
import com.ttvnp.ttj_asset_android_client.presentation.R

class SettingTermsOfConditionActivity : BaseActivity() {

    private lateinit var webViewTOC: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_condition)

        webViewTOC = findViewById(R.id.web_view_tom)
        webViewTOC.loadUrl("file:///android_asset/sen_token_tos.html")

        val toolbar = findViewById<Toolbar>(R.id.toolbar_terms_of_condition)
        toolbar.title = getString(R.string.title_setting_terms_of_conditions)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}
