package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Switch
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsSecurityPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsSecurityPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class SettingsSecurityActivity : BaseActivity(), SettingsSecurityPresenterTarget {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SettingsSecurityActivity::class.java)
            context?.startActivity(intent)
        }
    }

    @Inject
    lateinit var mPresenter: SettingsSecurityPresenter

    private lateinit var mToolbar: Toolbar
    private lateinit var mRequirePasswordOnSendSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_security)
        initView()
        mPresenter.init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dispose()
    }

    override fun bindUserInfo(userModel: UserModel) {
        mRequirePasswordOnSendSwitch.isChecked = userModel.requirePasswordOnSend
    }

    private fun initView() {
        mToolbar = findViewById(R.id.toolbar)
        mRequirePasswordOnSendSwitch = findViewById(R.id.switch_require_password_on_send)
        setupToolbar()
        setListener()
    }

    private fun setupToolbar() {
        mToolbar.title = getString(R.string.security)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mToolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun setListener() {
        mRequirePasswordOnSendSwitch.setOnClickListener {
            mPresenter.updateSecuritySettings(mRequirePasswordOnSendSwitch.isChecked)
        }
    }

}
