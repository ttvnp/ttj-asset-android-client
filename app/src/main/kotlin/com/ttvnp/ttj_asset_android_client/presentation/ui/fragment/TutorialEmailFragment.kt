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

class TutorialEmailFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialEmailFragment {
            return TutorialEmailFragment()
        }
    }

    var submitButtonClickHandler: View.OnClickListener? = null

    private lateinit var textInputLayoutTutorialEmailAddress: TextInputLayout
    private lateinit var textTutorialEmailAddress: TextInputEditText

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_email, container, false)
        view.findViewById<Button>(R.id.button_tutorial_submit).setOnClickListener(submitButtonClickHandler)
        textInputLayoutTutorialEmailAddress = view.findViewById<TextInputLayout>(R.id.text_input_layout_tutorial_email_address)
        textTutorialEmailAddress = view.findViewById<TextInputEditText>(R.id.text_tutorial_email_address)
        return view
    }

    fun getEmailAddressText(): String {
        return textTutorialEmailAddress.text?.toString()?:""
    }

    fun showValidationError(errorMessage: String) {
        textInputLayoutTutorialEmailAddress.isErrorEnabled = true
        textInputLayoutTutorialEmailAddress.error = errorMessage
    }
}
