package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.WebView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.util.getCurrentLocale
import java.util.*

class SettingTermsOfServiceActivity : BaseActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SettingTermsOfServiceActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var webViewTOC: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_terms_of_service)

        webViewTOC = findViewById(R.id.web_view_tom)
        var tos = "sen_token_tos.html"
        if (getCurrentLocale(resources) == Locale.JAPAN) {
            tos = "ja_terms_of_services.html"
        } else if (getCurrentLocale(resources) == Locale("vi")) {
            tos = "vn_terms_of_services.html"
        }
        webViewTOC.loadUrl("file:///android_asset/$tos")

        val toolbar = findViewById<Toolbar>(R.id.toolbar_terms_of_condition)
        toolbar.title = getString(R.string.title_setting_terms_of_service)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}
