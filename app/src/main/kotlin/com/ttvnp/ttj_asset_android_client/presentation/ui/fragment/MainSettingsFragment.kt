package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsNotificationActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.SettingMenuViewAdapter

class MainSettingsFragment : BaseMainFragment() {

    companion object {
        fun getInstance() : MainSettingsFragment {
            return MainSettingsFragment()
        }
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_settings, container, false)

        val layoutManager = LinearLayoutManager(this.context)
        val recyclerViewSettingMenu = view.findViewById<RecyclerView>(R.id.recycler_view_setting_menu)
        recyclerViewSettingMenu.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerViewSettingMenu.context, layoutManager.orientation)
        recyclerViewSettingMenu.addItemDecoration(dividerItemDecoration)

        val menuStrings = listOf(
                getString(R.string.menu_settings_profile),
                getString(R.string.menu_settings_notifications)
        )
        val adapter = SettingMenuViewAdapter(menuStrings);
        adapter.setItemOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                view?.let {
                    when (it.getId()) {
                        0 -> {
                            // case profile clicked.
                            val intent = Intent(activity, SettingsProfileActivity::class.java)
                            startActivity(intent)
                        }
                        1 -> {
                            // case notifications clicked.
                            val intent = Intent(activity, SettingsNotificationActivity::class.java)
                            startActivity(intent)
                        }
                        else -> { /* do nothing */ }
                    }
                }
            }
        })
        recyclerViewSettingMenu.adapter = adapter

        return view
    }
}