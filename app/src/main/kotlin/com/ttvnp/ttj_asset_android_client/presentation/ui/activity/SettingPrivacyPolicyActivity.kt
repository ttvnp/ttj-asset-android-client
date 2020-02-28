package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.webkit.WebView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.util.getCurrentLocale
import java.util.*

class SettingPrivacyPolicyActivity : BaseActivity() {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SettingPrivacyPolicyActivity::class.java)
            context?.startActivity(intent)
        }
    }

    private lateinit var webViewPP: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_privacy_policy)
        webViewPP = findViewById(R.id.web_view_pp)
        var pp = "sen_token_privacy_policy.html"
        if (getCurrentLocale(resources) == Locale.JAPAN) {
            pp = "ja_privacy_of_policy.html"
        }
        if (getCurrentLocale(resources) == Locale("vi")){
            pp = "vn_privacy_of_policy.html"
        }
        webViewPP.loadUrl("file:///android_asset/$pp")

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
