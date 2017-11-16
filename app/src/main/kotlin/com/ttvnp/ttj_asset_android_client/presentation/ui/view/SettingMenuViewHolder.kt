package com.ttvnp.ttj_asset_android_client.presentation.ui.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R

class SettingMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textMenu: TextView

    init {
        textMenu = itemView.findViewById<TextView>(R.id.text_menu)
    }
}