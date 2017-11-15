package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.ttvnp.ttj_asset_android_client.presentation.R

class TutorialEndFragment : Fragment() {

    companion object {
        fun getInstance() : TutorialEndFragment {
            return TutorialEndFragment()
        }
    }

    var appStartButtonClickHandler: View.OnClickListener? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_tutorial_end, container, false)

        val buttonAppStart = view.findViewById<Button>(R.id.button_tutorial_app_start)
        buttonAppStart.setOnClickListener(appStartButtonClickHandler)

        return view
    }
}
