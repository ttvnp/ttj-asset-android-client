package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.listener.getOnFocusChangeListener

class TutorialEmailFragment : Fragment() {

    companion object {
        fun getInstance(): TutorialEmailFragment {
            return TutorialEmailFragment()
        }
    }

    var submitButtonClickHandler: View.OnClickListener? = null
    var termsOfServiceListener: View.OnClickListener? = null

    private lateinit var textInputLayoutTutorialEmailAddress: TextInputLayout
    private lateinit var textTutorialEmailAddress: TextInputEditText
    private lateinit var buttonTutorialSubmit: Button
    private lateinit var ckTermsOfService: CheckBox
    private lateinit var tvTermsOfService: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_tutorial_email, container, false)

        buttonTutorialSubmit = view.findViewById(R.id.button_tutorial_submit)
        buttonTutorialSubmit.setOnClickListener(submitButtonClickHandler)
        textInputLayoutTutorialEmailAddress = view.findViewById(R.id.text_input_layout_tutorial_email_address)
        textTutorialEmailAddress = view.findViewById(R.id.text_tutorial_email_address)
        textTutorialEmailAddress.onFocusChangeListener = getOnFocusChangeListener(getString(R.string.email_address))
        ckTermsOfService = view.findViewById(R.id.chk_terms_of_service)
        ckTermsOfService.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked) {
                setEnableButton(true)
                return@setOnCheckedChangeListener
            }

            setEnableButton(false)
        })
        tvTermsOfService = view.findViewById(R.id.text_terms_of_service)
        tvTermsOfService.setOnClickListener(termsOfServiceListener)

        return view
    }

    fun getEmailAddressText(): String {
        return textTutorialEmailAddress.text?.toString() ?: ""
    }

    fun showValidationError(errorMessage: String) {
        textInputLayoutTutorialEmailAddress.isErrorEnabled = true
        textInputLayoutTutorialEmailAddress.error = errorMessage
    }

    private fun setEnableButton(value: Boolean) {
        var textColor = R.color.md_grey_500

        if (value) {
            textColor = R.color.colorTextOnPrimary
        }

        buttonTutorialSubmit.isEnabled = value
        buttonTutorialSubmit.setTextColor(ContextCompat.getColor(buttonTutorialSubmit.context, textColor))
    }

}
