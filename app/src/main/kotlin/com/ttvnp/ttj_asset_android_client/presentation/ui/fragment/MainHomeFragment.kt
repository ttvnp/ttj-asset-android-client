package com.ttvnp.ttj_asset_android_client.presentation.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserTransactionsModel
import com.ttvnp.ttj_asset_android_client.domain.util.formatString
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.activity.SettingsProfileActivity
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.PaymentHistoryViewAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.listener.EndlessScrollListener
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.MainHomePresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.MainHomePresenterTarget
import dagger.android.support.AndroidSupportInjection
import de.hdodenhof.circleimageview.CircleImageView
import javax.inject.Inject

class MainHomeFragment : BaseMainFragment(), MainHomePresenterTarget {

    @Inject
    lateinit var mainHomePresenter: MainHomePresenter

    private var textEmailAddress: TextView? = null
    private var profileImage: CircleImageView? = null
    private var textPointAmount: TextView? = null
    private var textCoinAmount: TextView? = null
    private var swipeLayoutPaymentHistory: SwipeRefreshLayout? = null
    private var swipeLayoutEmptyPaymentHistory: SwipeRefreshLayout? = null
    private var recyclerViewPaymentHistory: RecyclerView? = null
    private var emptyViewPaymentHistory: ListView? = null
    private var emptyTextViewPaymentHistory: TextView? = null
    private lateinit var progressBar: ProgressBar

    companion object {
        fun getInstance(): MainHomeFragment {
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
    ): View {
        val view = inflater.inflate(R.layout.fragment_main_home, container, false)
        textEmailAddress = view.findViewById(R.id.text_email_address)
        profileImage = view.findViewById(R.id.profile_image)
        textPointAmount = view.findViewById(R.id.text_point_amount)
        textCoinAmount = view.findViewById(R.id.text_coint_amount)
        swipeLayoutPaymentHistory = view.findViewById(R.id.swipe_layout_payment_history)
        swipeLayoutEmptyPaymentHistory = view.findViewById(R.id.swipe_layout_empty_payment_history)
        recyclerViewPaymentHistory = view.findViewById(R.id.recycler_view_payment_history)
        emptyViewPaymentHistory = view.findViewById(R.id.empty_view_payment_history)
        progressBar = view.findViewById(R.id.progress_bar)

        val layoutManager = LinearLayoutManager(this.context)
        recyclerViewPaymentHistory?.layoutManager = layoutManager
        val dividerItemDecoration = DividerItemDecoration(recyclerViewPaymentHistory?.context, layoutManager.orientation)
        recyclerViewPaymentHistory?.addItemDecoration(dividerItemDecoration)

        mainHomePresenter.setupUserInfo(false)
        mainHomePresenter.setupBalanceInfo(true)
        mainHomePresenter.setupUserTransactions(true)

        val swipeLayoutRefreshListener: () -> Unit = fun() {
            mainHomePresenter.setupBalanceInfo(true)
            mainHomePresenter.setupUserTransactions(true)
        }
        swipeLayoutPaymentHistory?.setOnRefreshListener(swipeLayoutRefreshListener)
        swipeLayoutEmptyPaymentHistory?.setOnRefreshListener(swipeLayoutRefreshListener)
        emptyTextViewPaymentHistory = view.findViewById(R.id.empty_text_view_payment_history)
        emptyViewPaymentHistory?.emptyView = emptyTextViewPaymentHistory
        emptyViewPaymentHistory?.emptyView?.visibility = View.GONE

        textEmailAddress?.setOnClickListener {
            mainHomePresenter.onProfileAreaClicked()
        }
        profileImage?.setOnClickListener {
            mainHomePresenter.onProfileAreaClicked()
        }
        textPointAmount?.setOnClickListener {
            mainHomePresenter.onProfileAreaClicked()
        }
        textCoinAmount?.setOnClickListener {
            mainHomePresenter.onProfileAreaClicked()
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        mainHomePresenter.dispose()
    }

    override fun bindUserInfo(userModel: UserModel) {
        textEmailAddress?.text = userModel.emailAddress
        if (userModel.profileImageURL.isEmpty()) return
        Glide
                .with(this.context)
                .load(userModel.profileImageURL)
                .into(profileImage)
    }

    override fun bindBalanceInfo(balancesModel: BalancesModel) {
        textPointAmount?.text = balancesModel.pointBalance.amount.formatString()
        textCoinAmount?.text = balancesModel.coinBalance.amount.formatString()
    }

    override fun bindUserTransactions(userTransactionsModel: UserTransactionsModel, forceRefresh: Boolean) {
        stopSwipeLayoutRefreshing()
        progressBar.visibility = View.GONE
        if (userTransactionsModel.userTransactions.isEmpty()) {
            // case empty
            swipeLayoutPaymentHistory?.visibility = View.GONE
            swipeLayoutEmptyPaymentHistory?.visibility = View.VISIBLE
            emptyTextViewPaymentHistory?.visibility = View.VISIBLE
        } else {
            // has data
            var adapter = recyclerViewPaymentHistory?.adapter
            if (adapter != null && adapter is PaymentHistoryViewAdapter) {
                adapter.addAllUserTransactionModel(userTransactionsModel.userTransactions)
            } else {
                adapter = PaymentHistoryViewAdapter(userTransactionsModel.userTransactions.toMutableList())
            }
            recyclerViewPaymentHistory?.adapter = adapter
            swipeLayoutPaymentHistory?.visibility = View.VISIBLE
            swipeLayoutEmptyPaymentHistory?.visibility = View.GONE
            emptyTextViewPaymentHistory?.visibility = View.GONE
            if (userTransactionsModel.hasMore) {
                var lastUserTransactionID = userTransactionsModel.userTransactions.last().id
                recyclerViewPaymentHistory?.addOnScrollListener(object : EndlessScrollListener(recyclerViewPaymentHistory?.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(currentPage: Int) {
                        // get last item
                        val sizeBeforeLoad = adapter.itemCount
                        mainHomePresenter.loadMoreUserTransactions(lastUserTransactionID, { loadedModel ->
                            if (!loadedModel.hasMore) this.setFinished()
                            adapter.addAllUserTransactionModel(loadedModel.userTransactions)
                            val updatedSize = loadedModel.userTransactions.size
                            recyclerViewPaymentHistory?.post { adapter.notifyItemRangeInserted(sizeBeforeLoad, updatedSize) }
                            lastUserTransactionID = loadedModel.userTransactions.last().id
                        }, forceRefresh)
                    }
                })
            }
        }
    }

    override fun gotoProfileDetail() {
        // case profile clicked.
        val intent = Intent(activity, SettingsProfileActivity::class.java)
        startActivity(intent)
    }

    override fun showError(throwable: Throwable) {
        super.showError(throwable)
        stopSwipeLayoutRefreshing()
    }

    private fun stopSwipeLayoutRefreshing() {
        swipeLayoutPaymentHistory?.isRefreshing = false
        swipeLayoutEmptyPaymentHistory?.isRefreshing = false
    }

}
