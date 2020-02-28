package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import android.widget.Button
import android.widget.EditText
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SettingsChangePasswordPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SettingsChangePasswordPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class SettingsChangePasswordActivity : BaseActivity(), SettingsChangePasswordPresenterTarget {

    companion object {
        fun start(context: Context?) {
            val intent = Intent(context, SettingsChangePasswordActivity::class.java)
            context?.startActivity(intent)
        }
    }

    @Inject
    lateinit var mPresenter: SettingsChangePasswordPresenter

    private lateinit var mToolbar: Toolbar
    private lateinit var mTextInputOldPassword: EditText
    private lateinit var mTextInputNewPassword: EditText
    private lateinit var mTextInputRetypePassword: EditText
    private lateinit var mBtnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_change_password)
        mPresenter.init(this)
        initView()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPresenter.dispose()
    }

    override fun onChangePasswordSuccessful() {
        finish()
    }

    override fun onValidateForm(
            oldPasswordError: String?,
            newPasswordError: String?,
            retypePasswordError: String?
    ) {
        oldPasswordError?.let { mTextInputOldPassword.error = it }
        newPasswordError?.let { mTextInputNewPassword.error = it }
        retypePasswordError?.let { mTextInputRetypePassword.error = it }
    }

    private fun initView() {
        mToolbar = findViewById(R.id.toolbar_change_password)
        mTextInputOldPassword = findViewById(R.id.text_input_old_password)
        mTextInputNewPassword = findViewById(R.id.text_input_new_password)
        mTextInputRetypePassword = findViewById(R.id.text_input_retype_password)
        mBtnSave = findViewById(R.id.button_save)
        initToolbar()
        setListener()
    }

    private fun initToolbar() {
        mToolbar.title = getString(R.string.title_change_password)
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        mToolbar.setNavigationOnClickListener {
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    private fun setListener() {
        mBtnSave.setOnClickListener {
            val oldPassword = mTextInputOldPassword.text.toString()
            val newPassword = mTextInputNewPassword.text.toString()
            val retypePassword = mTextInputRetypePassword.text.toString()
            if (!mPresenter.isValidated(this, oldPassword, newPassword, retypePassword)) return@setOnClickListener
            mPresenter.changePassword(oldPassword, newPassword, retypePassword)
        }
    }

}
