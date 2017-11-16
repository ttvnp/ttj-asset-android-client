package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.SettingMenuViewHolder

class SettingMenuViewAdapter(
        private val data: List<String>
) : RecyclerView.Adapter<SettingMenuViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingMenuViewHolder {
        context = parent!!.context
        val inflater = LayoutInflater.from(parent.context).
                inflate(R.layout.view_setting_menu_row, parent, false)
        return SettingMenuViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: SettingMenuViewHolder, position: Int) {
        holder.textMenu.text = data.get(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}