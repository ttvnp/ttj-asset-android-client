package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionsModel
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject
import com.ttvnp.ttj_asset_android_client.domain.util.formatString
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.PaymentHistoryViewAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.listener.EndlessScrollListener

class MainHomeFragment : BaseMainFragment(), MainHomePresenterTarget {

    @Inject
    lateinit var mainHomePresenter: MainHomePresenter

    private lateinit var textEmailAddress: TextView
    private lateinit var profileImage: CircleImageView
    private lateinit var textPointAmount: TextView
    private lateinit var textCoinAmount: TextView
    private lateinit var recyclerViewPaymentHistory: RecyclerView
    private lateinit var emptyViewPaymentHistory: TextView

    companion object {
        fun getInstance() : MainHomeFragment {
            return MainHomeFragment()
        }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        mainHomePresenter.init(this)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ) : View {
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        textEmailAddress = view.findViewById<TextView>(R.id.text_email_address)
        profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        textPointAmount = view.findViewById<TextView>(R.id.text_point_amount)
        textCoinAmount = view.findViewById<TextView>(R.id.text_coint_amount)
        recyclerViewPaymentHistory = view.findViewById<RecyclerView>(R.id.recycler_view_payment_history)
        emptyViewPaymentHistory = view.findViewById<TextView>(R.id.empty_view_payment_history)

        val layoutManager = LinearLayoutManager(this.context)
        recyclerViewPaymentHistory.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerViewPaymentHistory.context, layoutManager.orientation)
        recyclerViewPaymentHistory.addItemDecoration(dividerItemDecoration)

        mainHomePresenter.setupUserInfo()
        mainHomePresenter.setupBalanceInfo()
        mainHomePresenter.setupUserTransactions()
        return view
    }

    override fun bindUserInfo(userModel: UserModel) {
        textEmailAddress.text = userModel.emailAddress
        if (0 < userModel.profileImageURL.length) {
            Picasso.with(this.context).load(userModel.profileImageURL).into(profileImage)
        }
    }

    override fun bindBalanceInfo(balancesModel: BalancesModel) {
        textPointAmount.text = balancesModel.pointBalance.amount.formatString()
        textCoinAmount.text = balancesModel.coinBalance.amount.formatString()
    }

    override fun bindUserTransactions(userTransactionsModel: UserTransactionsModel) {
        if (userTransactionsModel.userTransactions.size < 1) {
            // case empty
            recyclerViewPaymentHistory.visibility = View.GONE
            emptyViewPaymentHistory.visibility = View.VISIBLE
        } else {
            // has data
            val adapter = PaymentHistoryViewAdapter(userTransactionsModel.userTransactions.toMutableList());
            recyclerViewPaymentHistory.adapter = adapter
            recyclerViewPaymentHistory.visibility = View.VISIBLE
            emptyViewPaymentHistory.visibility = View.GONE
            if (userTransactionsModel.hasMore) {
                recyclerViewPaymentHistory.addOnScrollListener(object : EndlessScrollListener(recyclerViewPaymentHistory.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        // get last item
                        val lastUserTransactionID = userTransactionsModel.userTransactions.last().id
                        val initialSize = userTransactionsModel.userTransactions.size
                        mainHomePresenter.loadMoreUserTransactions(lastUserTransactionID, { loadedModel ->
                            if (!loadedModel.hasMore) this.setFinished()
                            adapter.addAllUserTransactionModel(loadedModel.userTransactions)
                            val updatedSize = adapter.itemCount
                            recyclerViewPaymentHistory.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
                        })
                    }
                })
            }
        }
    }
}
