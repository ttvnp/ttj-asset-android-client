package com.ttvnp.ttj_asset_android_client.presentation.ui.adapter

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.ttvnp.ttj_asset_android_client.domain.model.TransactionStatus
import com.ttvnp.ttj_asset_android_client.domain.model.TransactionType
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionModel
import com.ttvnp.ttj_asset_android_client.domain.util.formatString
import com.ttvnp.ttj_asset_android_client.domain.util.prependIfNotBlank
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.FooterLoadingViewHolder
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.PaymentHistoryViewHolder
import java.text.SimpleDateFormat
import java.util.*

class PaymentHistoryViewAdapter(
        private val data: MutableList<UserTransactionModel>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context
    var progressBar: ProgressBar? = null
    var lastSize: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        if (viewType == PaymentHistory.FOOTER.rawValue) {
            val inflater = LayoutInflater.from(parent.context).inflate(R.layout.view_footer_loading, parent, false)
            return FooterLoadingViewHolder(inflater)
        }
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.view_payment_history_row, parent, false)
        return PaymentHistoryViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is PaymentHistoryViewHolder) {
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
            } else {
                holder.textHistoryTargetUser.visibility = View.VISIBLE
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
        } else if (holder is FooterLoadingViewHolder) {
            if (position > 20) {
                progressBar = holder.progressBar
            }
            if (lastSize == data.size) {
                holder.progressBar.visibility = View.GONE
            } else {
                lastSize = data.size
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) PaymentHistory.FOOTER.rawValue else PaymentHistory.BODY.rawValue
    }

    fun addAllUserTransactionModel(models: Collection<UserTransactionModel>) {
        // merge
        models.forEach { add ->
            val pos: Int
            val target = data.firstOrNull { d -> d.id == add.id }
            if (target == null) {
                data.add(add)
                pos = data.size - 1
            } else {
                pos = data.indexOfFirst { d -> d.id == add.id }
                data[pos] = add
            }
            notifyItemChanged(pos)
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

enum class PaymentHistory(val rawValue: Int) {
    BODY(1),
    FOOTER(2)
}
