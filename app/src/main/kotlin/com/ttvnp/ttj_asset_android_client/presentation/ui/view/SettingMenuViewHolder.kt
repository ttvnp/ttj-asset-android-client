package com.ttvnp.ttj_asset_android_client.presentation.ui.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R

class SettingMenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val linearLayoutMenu: LinearLayout
    val textMenu: TextView

    init {
        linearLayoutMenu = itemView.findViewById<LinearLayout>(R.id.linear_layout_menu)
        textMenu = itemView.findViewById<TextView>(R.id.text_menu)
    }
}