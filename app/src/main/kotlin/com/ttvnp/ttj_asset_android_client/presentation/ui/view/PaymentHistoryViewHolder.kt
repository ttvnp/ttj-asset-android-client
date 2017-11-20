package com.ttvnp.ttj_asset_android_client.presentation.ui.view

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.ttvnp.ttj_asset_android_client.R
import de.hdodenhof.circleimageview.CircleImageView

class PaymentHistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val imageTargetUserProfile: CircleImageView
    val textHistoryDate: TextView
    val textHistoryTargetUser: TextView
    val textHistoryDetail: TextView

    init {
        imageTargetUserProfile = itemView.findViewById<CircleImageView>(R.id.image_target_user_profile)
        textHistoryDate = itemView.findViewById<TextView>(R.id.text_history_date)
        textHistoryTargetUser = itemView.findViewById<TextView>(R.id.text_history_target_user)
        textHistoryDetail = itemView.findViewById<TextView>(R.id.text_history_detail)
    }
}
