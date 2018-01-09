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
import com.ttvnp.ttj_asset_android_client.R

class TutorialEmailFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialEmailFragment {
            return TutorialEmailFragment()
        }
    }

    var submitButtonClickHandler: View.OnClickListener? = null
    var termsAndConditionsClickHandler: View.OnClickListener? = null

    private lateinit var textInputLayoutTutorialEmailAddress: TextInputLayout
    private lateinit var textTutorialEmailAddress: TextInputEditText
    private lateinit var buttonTutorialSubmit: Button
    private lateinit var chkTermsAndConditions: CheckBox
    private lateinit var tvTermsAndConditions: TextView

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_email, container, false)

        buttonTutorialSubmit = view.findViewById(R.id.button_tutorial_submit)
        buttonTutorialSubmit.setOnClickListener(submitButtonClickHandler)
        textInputLayoutTutorialEmailAddress = view.findViewById(R.id.text_input_layout_tutorial_email_address)
        textTutorialEmailAddress = view.findViewById(R.id.text_tutorial_email_address)
        chkTermsAndConditions = view.findViewById(R.id.chkTermAndConditions)
        chkTermsAndConditions.setOnCheckedChangeListener({
            _, isChecked ->
            if (isChecked) {
                setEnableButton(true)
                return@setOnCheckedChangeListener
            }

            setEnableButton(false)
        })
        tvTermsAndConditions = view.findViewById(R.id.tvTermsAndConditions)
        tvTermsAndConditions.setOnClickListener(termsAndConditionsClickHandler)

        return view
    }

    fun getEmailAddressText(): String {
        return textTutorialEmailAddress.text?.toString()?:""
    }

    fun showValidationError(errorMessage: String) {
        textInputLayoutTutorialEmailAddress.isErrorEnabled = true
        textInputLayoutTutorialEmailAddress.error = errorMessage
    }

    private fun setEnableButton(value: Boolean) {
        buttonTutorialSubmit.isEnabled = value

        if (value) {
            buttonTutorialSubmit.setBackgroundColor(
                    ContextCompat.getColor(
                            context,
                            R.color.colorPrimary
                    )
            )
            return
        }

        buttonTutorialSubmit.setBackgroundColor(
                ContextCompat.getColor(
                        context,
                        R.color.md_grey_500
                )
        )
    }

}
