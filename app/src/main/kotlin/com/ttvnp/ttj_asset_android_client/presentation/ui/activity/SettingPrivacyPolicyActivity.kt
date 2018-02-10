package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.webkit.WebView
import com.ttvnp.ttj_asset_android_client.presentation.R

class SettingPrivacyPolicyActivity : BaseActivity() {

    private lateinit var webViewPP: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_privacy_policy)
        webViewPP = findViewById(R.id.web_view_pp)
        webViewPP.loadUrl("file:///android_asset/sen_token_privacy_policy.html")

        val toolbar = findViewById<Toolbar>(R.id.toolbar_privacy_policy)
        toolbar.title = getString(R.string.title_setting_privacy_policy)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

}
