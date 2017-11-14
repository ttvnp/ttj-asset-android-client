package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.View
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.SendAmountFormFragment
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class SendActivity : BaseActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

    companion object {
        val INTENT_EXTRA_KEY = "qr_string"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_send)
        toolbar.title = getString(R.string.title_send)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)

        val cancel: () -> Unit = {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent);
            finish()
        }

        toolbar.setNavigationOnClickListener {
            cancel()
        }

        if (savedInstanceState != null) return
        val intent = getIntent()
        val qrString = intent.getStringExtra(INTENT_EXTRA_KEY)
        val formFragment = SendAmountFormFragment.getInstance(qrString)
        formFragment.cancelButtonClickHandler = object : View.OnClickListener {
            override fun onClick(v: View?) {
                cancel()
            }
        }
        supportFragmentManager
                .beginTransaction()
                .add(R.id.send_activity_fragment_container, formFragment)
                .commit()
    }
}
