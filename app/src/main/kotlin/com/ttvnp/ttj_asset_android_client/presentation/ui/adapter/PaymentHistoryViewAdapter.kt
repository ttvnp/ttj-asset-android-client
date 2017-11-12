package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.TransactionType
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionModel
import com.ttvnp.ttj_asset_android_client.domain.util.formatString
import com.ttvnp.ttj_asset_android_client.domain.util.prependIfNotBlank
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.PaymentHistoryViewHolder
import java.text.SimpleDateFormat
import java.util.*


class PaymentHistoryViewAdapter(
        private val data: MutableList<UserTransactionModel>
) : RecyclerView.Adapter<PaymentHistoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PaymentHistoryViewHolder {
        context = parent!!.context
        val inflater = LayoutInflater.from(parent.context).
                inflate(R.layout.view_payment_history_row, parent, false)
        return PaymentHistoryViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        val model = data.get(position)
        if (0 < model.targetUserProfileImageURL.length) {
            Picasso.with(context).load(model.targetUserProfileImageURL).into(holder.imageTargetUserProfile)
        }

        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        holder.textHistoryDate.text = format.format(model.loggedAt)

        when (model.transactionType) {
            TransactionType.RECEIVE -> {
                // holder.textHistoryDetail.setCompoundDrawablesWithIntrinsicBounds()
            }
            TransactionType.SEND -> {
                // holder.textHistoryDetail.setCompoundDrawablesWithIntrinsicBounds()
            }
        }
        holder.textHistoryTargetUser.text = buildTargetUserText(model)
        holder.textHistoryDetail.text = "%s %s".format(
                model.amount.formatString(),
                model.assetType.rawValue
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addAllUserTransactionModel(models: Collection<UserTransactionModel>) {
        data.addAll(models)
    }

    private fun buildTargetUserText(model: UserTransactionModel): String {
        var userName = ""
        if (!model.targetUserFirstName.isBlank()) {
            userName += model.targetUserFirstName.prependIfNotBlank(" ")
        }
        if (!model.targetUserMiddleName.isBlank()) {
            userName += model.targetUserMiddleName.prependIfNotBlank(" ")
        }
        if (!model.targetUserLastName.isBlank()) {
            userName += model.targetUserLastName.prependIfNotBlank(" ")
        }
        return if (userName.isBlank()) model.targetUserEmailAddress else userName
    }
}
