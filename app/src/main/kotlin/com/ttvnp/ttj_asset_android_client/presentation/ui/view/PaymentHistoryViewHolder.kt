package com.ttvnp.ttj_asset_android_client.presentation.ui.view

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.presentation.R
import de.hdodenhof.circleimageview.CircleImageView

class PaymentHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val layoutPaymentHistory: LinearLayout = itemView.findViewById(R.id.layout_payment_history)
    val imageTargetUserProfile: CircleImageView = itemView.findViewById(R.id.image_target_user_profile)
    val imageTransactionType: ImageView = itemView.findViewById(R.id.image_transaction_type)
    val textHistoryDate: TextView = itemView.findViewById(R.id.text_history_date)
    val textHistoryTargetUser: TextView = itemView.findViewById(R.id.text_history_target_user)
    val textHistoryDetail: TextView = itemView.findViewById(R.id.text_history_detail)
    val imageTransactionStatus: ImageView = itemView.findViewById(R.id.image_transaction_status)
}

class FooterLoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
}