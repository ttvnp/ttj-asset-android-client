package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.R


class SettingsProfileEditBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private var folderOnClickListener: View.OnClickListener? = null
    fun setFolderOnClickListener(listener: View.OnClickListener) {
        folderOnClickListener = listener
    }

    private var cameraOnClickListener: View.OnClickListener? = null
    fun setCameraOnClickListener(listener: View.OnClickListener) {
        cameraOnClickListener = listener
    }


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
        val textFromFolder = view.findViewById<TextView>(R.id.text_from_folder)
        textFromFolder.setOnClickListener(folderOnClickListener)
        val textFromCamera = view.findViewById<TextView>(R.id.text_from_camera)
        textFromCamera.setOnClickListener(cameraOnClickListener)
        return view
    }

}