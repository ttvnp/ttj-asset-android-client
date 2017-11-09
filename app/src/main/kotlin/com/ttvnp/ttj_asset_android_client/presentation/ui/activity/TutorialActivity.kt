package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ttvnp.ttj_asset_android_client.domain.exceptions.BaseException
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.presentation.R
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialEndFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialFirstFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.TutorialViewPager
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.TutorialViewPagerAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialCodeFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialEmailFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.TutorialPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.TutorialPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class TutorialActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, TutorialPresenterTarget {

    @Inject
    lateinit var tutorialPresenter : TutorialPresenter

    private var viewPager : TutorialViewPager? = null
    private var floatingIndicatorView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        initViewPager()
        setupIndicator()
        tutorialPresenter.onCreate(this)
    }

    override fun showError(throwable: Throwable) {
        when (throwable) {
            is BaseException -> AlertDialog
                    .Builder(this)
                    .setTitle(resources.getString(R.string.error_dialog_title))
                    .setMessage(resources.getString(R.string.error_device_registration))
                    .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                    .show()
            else -> AlertDialog
                    .Builder(this)
                    .setTitle(resources.getString(R.string.error_dialog_title))
                    .setMessage(resources.getString(R.string.error_device_registration))
                    .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                    .show()
        }
    }

    override fun gotoRegisterEmailPage() {
        toPage(1)
    }

    override fun showValidationError(ve: ValidationException) {
        AlertDialog
                .Builder(this)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(resources.getString(R.string.error_validation_email_address))
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
    }

    override fun gotoVerifyEmailPage() {
        toPage(2)
    }

    override fun gotoEndPage() {
        toPage(3)
    }

    private fun toPage(page: Int = 0) {
        viewPager?.let {
            it.setCurrentItem(page, true)
        }
    }

    private fun initViewPager() {
        findViewById<TutorialViewPager>(R.id.view_pager).let {
            viewPager = it
            it.scrollDirection = TutorialViewPager.SCROLL_PREV
            val fragmentManager = getSupportFragmentManager()
            val adapter = TutorialViewPagerAdapter(fragmentManager)
            val firstFragment = TutorialFirstFragment.getInstance()
            firstFragment.startButtonClickHandler = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    tutorialPresenter.start()
                }
            }
            adapter.addFragment(firstFragment)

            val emailFragment = TutorialEmailFragment.getInstance()
            emailFragment.submitButtonClickHandler = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    tutorialPresenter.submitEmailAddress(emailFragment.getEmailAddressText())
                }
            }
            adapter.addFragment(emailFragment)

            val codeFragment = TutorialCodeFragment.getInstance()
            codeFragment.submitButtonClickHandler = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    tutorialPresenter.verifyEmailAddress(codeFragment.getVerificationCode())
                }
            }
            adapter.addFragment(codeFragment)

            val endFragment = TutorialEndFragment.getInstance()
            adapter.addFragment(endFragment)

            it.adapter = adapter
            it.addOnPageChangeListener(this)
        }
    }

    private fun setupIndicator() {
        findViewById<View>(R.id.view_floating_indicator).let {
            floatingIndicatorView = it
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        floatingIndicatorView?.let {
            val indicatorSpace = resources.getDimensionPixelSize(R.dimen.middle_margin).toFloat()
            it.translationX = position * indicatorSpace + positionOffset * indicatorSpace
        }
    }

    override fun onPageSelected(position: Int) {
    }
}
