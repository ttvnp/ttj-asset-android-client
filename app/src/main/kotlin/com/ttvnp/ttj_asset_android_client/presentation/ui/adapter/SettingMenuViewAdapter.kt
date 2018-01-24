package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.SettingMenuViewHolder

class SettingMenuViewAdapter(
        private val data: List<String>
) : RecyclerView.Adapter<SettingMenuViewHolder>() {

    private lateinit var context: Context
    private var itemOnClickListener: View.OnClickListener? = null
    private val linearLayoutMenus = arrayListOf<LinearLayout>()

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingMenuViewHolder {
        context = parent!!.context
        val inflater = LayoutInflater.from(parent.context).
                inflate(R.layout.view_setting_menu_row, parent, false)
        return SettingMenuViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: SettingMenuViewHolder, position: Int) {
        val item = data[position]

        if (item.isBlank()) {
            holder.linearLayoutMenu.setBackgroundColor(
                    ContextCompat.getColor(context, R.color.material_grey_100)
            )
        }

        holder.textMenu.text = item
        holder.linearLayoutMenu.isSelected = false
        holder.linearLayoutMenu.id = holder.adapterPosition
        linearLayoutMenus.add(holder.linearLayoutMenu)
        holder.linearLayoutMenu.setOnClickListener { view ->
            linearLayoutMenus.forEach {
                it.isSelected = false
            }
            linearLayoutMenus[holder.linearLayoutMenu.id].isSelected = true
            holder.linearLayoutMenu.isSelected = true
            itemOnClickListener?.onClick(view)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItemOnClickListener(itemOnClickListener: View.OnClickListener?) {
        this.itemOnClickListener = itemOnClickListener
    }
}