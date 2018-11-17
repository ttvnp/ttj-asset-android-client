package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.TransactionType
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionModel
import com.ttvnp.ttj_asset_android_client.domain.util.formatString
import com.ttvnp.ttj_asset_android_client.domain.util.prependIfNotBlank
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.domain.model.TransactionStatus
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.PaymentHistoryViewHolder
import java.text.SimpleDateFormat
import java.util.*


class PaymentHistoryViewAdapter(
        private val data: MutableList<UserTransactionModel>
) : RecyclerView.Adapter<PaymentHistoryViewHolder>() {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentHistoryViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.view_payment_history_row, parent, false)
        return PaymentHistoryViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: PaymentHistoryViewHolder, position: Int) {
        val model = data[position]
        if (model.targetUserProfileImageURL.isNotEmpty()) {
            Glide.with(context).load(model.targetUserProfileImageURL).into(holder.imageTargetUserProfile)
        } else {
            holder.imageTargetUserProfile.setImageResource(R.drawable.ic_user_default_profile_grey)
        }

        val format = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault())
        holder.textHistoryDate.text = format.format(model.loggedAt)

        when (model.transactionType) {
            TransactionType.RECEIVE -> {
                holder.imageTransactionType.setImageResource(R.drawable.ic_receive_arrow)
            }
            TransactionType.SEND -> {
                holder.imageTransactionType.setImageResource(R.drawable.ic_send_arrow)
            }
        }
        var historyDetail = "%s %s".format(
                model.amount.formatString(),
                model.assetType.rawValue
        )
        holder.textHistoryTargetUser.text = buildTargetUserText(model)
        if (model.targetUserStrAccountID.isNotBlank()) {
            holder.textHistoryTargetUser.visibility = View.GONE
            model.targetUserMemoText.let {
                if (it.isBlank()) {
                    if (model.targetUserMemoText.isBlank()) {
                        historyDetail = context.getString(R.string.history_accountID).format(model.targetUserStrAccountID) +
                                "\n" + historyDetail
                    }
                    return@let
                }
                historyDetail =
                        context.getString(R.string.history_accountID).format(model.targetUserStrAccountID) +
                        "\n" + context.getString(R.string.history_memo).format(model.targetUserMemoText) +
                        "\n" + historyDetail
            }
        }
        holder.textHistoryDetail.text = historyDetail
        when (model.transactionStatus) {
            TransactionStatus.Unprocessed -> {
                holder.layoutPaymentHistory.setBackgroundColor(ContextCompat.getColor(holder.layoutPaymentHistory.context, R.color.md_yellow_100))
                holder.imageTransactionStatus.visibility = View.VISIBLE
                holder.imageTransactionStatus.setImageResource(R.drawable.ic_pending)
            }
            TransactionStatus.Error -> {
                holder.layoutPaymentHistory.setBackgroundColor(ContextCompat.getColor(holder.layoutPaymentHistory.context, R.color.md_grey_300))
                holder.imageTransactionStatus.visibility = View.VISIBLE
                holder.imageTransactionStatus.setImageResource(R.drawable.ic_error)
            }
            else -> {
                holder.layoutPaymentHistory.setBackgroundColor(ContextCompat.getColor(holder.layoutPaymentHistory.context, R.color.md_white_1000))
                holder.imageTransactionStatus.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun addAllUserTransactionModel(models: Collection<UserTransactionModel>) {
        // merge
        models.forEach { add ->
            val target = data.firstOrNull { d -> d.id == add.id }
            if (target == null) {
                data.add(add)
            } else {
                data[data.indexOfFirst { d -> d.id == add.id }] = add
            }
        }
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
