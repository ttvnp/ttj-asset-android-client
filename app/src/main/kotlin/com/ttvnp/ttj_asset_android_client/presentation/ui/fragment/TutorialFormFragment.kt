package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ttvnp.ttj_asset_android_client.presentation.R

class TutorialFormFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialFormFragment {
            return TutorialFormFragment()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_form, container, false)
        return view
    }
}
