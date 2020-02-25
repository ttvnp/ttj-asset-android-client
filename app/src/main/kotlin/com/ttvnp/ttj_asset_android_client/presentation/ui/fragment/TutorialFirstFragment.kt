package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ttvnp.ttj_asset_android_client.presentation.R

class TutorialFirstFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialFirstFragment {
            return TutorialFirstFragment()
        }
    }
    var startButtonClickHandler: View.OnClickListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_first, container, false)
        view.findViewById<Button>(R.id.button_tutorial_start).let {
            it.setOnClickListener(startButtonClickHandler)
        }
        return view
    }
}
