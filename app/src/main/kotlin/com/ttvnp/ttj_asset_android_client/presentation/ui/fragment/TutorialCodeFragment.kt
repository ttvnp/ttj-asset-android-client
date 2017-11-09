package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ttvnp.ttj_asset_android_client.presentation.R

class TutorialCodeFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialCodeFragment {
            return TutorialCodeFragment()
        }
    }

    var submitButtonClickHandler: View.OnClickListener? = null

    private lateinit var textInputLayoutTutorialVerificationCode: TextInputLayout
    private lateinit var textTutorialVerificationCode: TextInputEditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_code, container, false)
        view.findViewById<Button>(R.id.button_tutorial_verification_code_submit).setOnClickListener(submitButtonClickHandler)
        view.findViewById<TextInputLayout>(R.id.text_input_layout_tutorial_verification_code).let {
            textInputLayoutTutorialVerificationCode = it
        }
        view.findViewById<TextInputEditText>(R.id.text_tutorial_verification_code).let {
            textTutorialVerificationCode = it
        }
        return view
    }

    fun getVerificationCode(): String {
        return textTutorialVerificationCode.text?.toString()?:""
    }

    fun showValidationError(errorMessage: String) {
        textInputLayoutTutorialVerificationCode.isErrorEnabled = true
        textInputLayoutTutorialVerificationCode.error = errorMessage
    }
}