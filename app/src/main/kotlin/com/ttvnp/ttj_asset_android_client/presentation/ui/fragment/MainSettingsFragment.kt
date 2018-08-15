package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.*
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.SettingMenuViewAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainSettingsPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.util.changeLocale
import dagger.android.support.AndroidSupportInjection
import java.util.*
import javax.inject.Inject


class MainSettingsFragment : BaseMainFragment() {

    companion object {
        fun getInstance(): MainSettingsFragment {
            return MainSettingsFragment()
        }
    }

    @Inject
    lateinit var mainSettingsPresenter: MainSettingsPresenter

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_main_settings, container, false)

        val layoutManager = LinearLayoutManager(this.context)
        val recyclerViewSettingMenu: RecyclerView = view.findViewById(R.id.recycler_view_setting_menu)
        val dividerItemDecoration = DividerItemDecoration(recyclerViewSettingMenu.context, layoutManager.orientation)
        recyclerViewSettingMenu.layoutManager = layoutManager
        recyclerViewSettingMenu.addItemDecoration(dividerItemDecoration)

        val menuStrings = listOf(
                getString(R.string.menu_settings_profile),
                "",
                getString(R.string.security),
                getString(R.string.menu_settings_notifications),
                getString(R.string.menu_settings_language),
                getString(R.string.title_setting_terms_of_service),
                getString(R.string.title_setting_privacy_policy),
                getString(R.string.title_change_password)
        )

        val adapter = SettingMenuViewAdapter(menuStrings)
        adapter.setItemOnClickListener(View.OnClickListener { v ->
            v?.let {
                when (it.id) {
                    0 -> {
                        // case profile clicked.
                        SettingsProfileActivity.start(context)
                    }
                    2 -> {
                        // case security
                        SettingsSecurityActivity.start(context)
                    }
                    3 -> {
                        // case notifications clicked.
                        SettingsNotificationActivity.start(context)
                    }
                    4 -> {
                        //change language dialog
                        showLanguagesDialog()
                    }
                    5 -> {
                        //case terms of conditions.
                        SettingTermsOfServiceActivity.start(context)
                    }
                    6 -> {
                        //case privacy policy
                        SettingPrivacyPolicyActivity.start(context)
                    }
                    7 -> {
                        // case change password clicked.
                        SettingsChangePasswordActivity.start(context)
                    }
                    else -> { /* do nothing */
                    }
                }
            }
        })
        recyclerViewSettingMenu.adapter = adapter

        return view
    }

    private fun showLanguagesDialog() {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.view_language_dialog)
        dialog.setTitle(getString(R.string.title_settings_language))
        dialog.setOnCancelListener { dialogInterface ->
            dialogInterface.dismiss()
        }
        val languages = resources.getStringArray(R.array.languages)
        val listLanguages: ListView = dialog.findViewById(R.id.list_languages)
        val adapter = ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                languages
        )
        listLanguages.adapter = adapter
        listLanguages.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {
                    changeLanguage(dialog, Locale.US)
                }
                1 -> {
                    changeLanguage(dialog, Locale.JAPAN)
                }
                else -> {
                    changeLanguage(dialog, Locale("vi"))
                }
            }
        }
        dialog.show()
    }

    private fun changeLanguage(dialog: Dialog, locale: Locale) {
        changeLocale(context.resources, locale)
        val fragment = MainSettingsFragment@ this
        fragment
                .fragmentManager
                .beginTransaction()
                .detach(fragment)
                .attach(fragment)
                .commit()
        val nearbyFragment = (activity as MainActivity).adapter.getItem(2) as MainSendFragment
        nearbyFragment
                .fragmentManager
                .beginTransaction()
                .detach(nearbyFragment)
                .attach(nearbyFragment)
                .commit()
        mainSettingsPresenter.saveLanguage(locale.language)
        dialog.dismiss()
    }

}