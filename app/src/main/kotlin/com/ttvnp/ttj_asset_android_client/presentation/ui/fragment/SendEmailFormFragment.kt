package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.SendEmailFormPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.SendEmailFormPresenterTarget
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SendEmailFormFragment() : BaseMainFragment(), SendEmailFormPresenterTarget {

    @Inject
    lateinit var sendEmailFormPresenter: SendEmailFormPresenter

    private lateinit var textInputLayoutSendEmail: TextInputLayout
    private lateinit var textSendEmail: TextView

    var cancelButtonClickHandler: View.OnClickListener? = null

    companion object {
        val QR_STRING_ARG_KEY = "qr_string"
        fun getInstance() : SendEmailFormFragment {
            return SendEmailFormFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_send_email_form, container, false)
        textInputLayoutSendEmail = view.findViewById(R.id.text_input_layout_send_email)
        textSendEmail = view.findViewById(R.id.text_send_email)
        val buttonCancel = view.findViewById<Button>(R.id.button_send_email_cancel)
        buttonCancel.setOnClickListener(cancelButtonClickHandler)
        val buttonSubmit = view.findViewById<Button>(R.id.button_send_email_submit)
        buttonSubmit.setOnClickListener {
            val emailAddress = textSendEmail.text.toString()
            sendEmailFormPresenter.checkEmailAddress(emailAddress, { otherUserModel ->
                val formFragment = SendAmountFormFragment.getInstance()
                formFragment.arguments = Bundle().apply {
                    this.putString(SendAmountFormFragment.QR_STRING_ARG_KEY, emailAddress)
                }
                formFragment.cancelButtonClickHandler = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        if (0 < fragmentManager.backStackEntryCount) {
                            fragmentManager.popBackStack()
                        }
                    }
                }
                fragmentManager
                        .beginTransaction()
                        .addToBackStack("")
                        .replace(R.id.send_activity_fragment_container, formFragment)
                        .commit()
            })
        }
        sendEmailFormPresenter.initialize(this)
        return view
    }

    override fun showNoSuchUser() {
        textInputLayoutSendEmail.isErrorEnabled = true
        textInputLayoutSendEmail.error = getString(R.string.error_message_no_such_email_address_user)
    }
}