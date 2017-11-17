package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ttvnp.ttj_asset_android_client.presentation.R


class SettingsProfileEditBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        fun getInstance() : SettingsProfileEditBottomSheetDialogFragment {
            return SettingsProfileEditBottomSheetDialogFragment()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.view_settings_profile_edit_bottom_sheet, container, false)
        return view
    }

}