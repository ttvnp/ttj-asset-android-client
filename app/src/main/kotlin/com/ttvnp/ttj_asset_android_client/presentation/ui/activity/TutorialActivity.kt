package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.RelativeLayout
import com.google.firebase.crash.FirebaseCrash
import com.ttvnp.ttj_asset_android_client.R
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.RegisterEmailResultModel
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialEndFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialFirstFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.view.ScrollControllViewPager
import com.ttvnp.ttj_asset_android_client.presentation.ui.adapter.TutorialViewPagerAdapter
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialCodeFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.fragment.TutorialEmailFragment
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.TutorialPresenter
import com.ttvnp.ttj_asset_android_client.presentation.ui.presenter.target.TutorialPresenterTarget
import dagger.android.AndroidInjection
import javax.inject.Inject

class TutorialActivity : BaseActivity(), ViewPager.OnPageChangeListener, TutorialPresenterTarget {

    @Inject
    lateinit var tutorialPresenter : TutorialPresenter

    private var emailFragment: TutorialEmailFragment? = null
    private var codeFragment: TutorialCodeFragment? = null
    private var endFragment: TutorialEndFragment? = null

    private var viewPager: ScrollControllViewPager? = null
    private var floatingIndicatorView: View? = null
    private var footer: RelativeLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        initViewPager()
        setupIndicator()
        tutorialPresenter.onCreate(this)
    }

    override fun showError(throwable: Throwable) {
        AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.error_dialog_title))
                .setMessage(resources.getString(R.string.error_default_message))
                .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                .show()
        FirebaseCrash.report(throwable)
    }

    override fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        when (errorCode) {
            ErrorCode.ERROR_CANNOT_REGISTER_DEVICE -> {
                AlertDialog.Builder(this)
                        .setTitle(resources.getString(R.string.error_dialog_title))
                        .setMessage(resources.getString(R.string.error_device_registration))
                        .setPositiveButton(resources.getString(R.string.default_positive_button_text), null)
                        .show()
                throwable?.let {
                    FirebaseCrash.log(it.message)
                }
            }
            ErrorCode.ERROR_VALIDATION_EMAIL -> {
                emailFragment?.showValidationError(getString(R.string.error_validation_email_address))
            }
            ErrorCode.ERROR_VALIDATION_VERIFICATION_CODE -> {
                codeFragment?.showCodeValidationError(getString(R.string.error_invalid_verification_code))
            }
            ErrorCode.ERROR_VALIDATION_PASSWORD_ON_IMPORT -> {
                codeFragment?.showPasswordValidationError(getString(R.string.error_invalid_password_on_import))
            }
            else -> {
                throwable?.let {
                    this.showError(it)
                }
            }
        }
    }

    override fun gotoRegisterEmailPage() {
        toPage(1)
        firebaseAnalyticsHelper?.logTutorialBeginEvent()
    }

    override fun gotoVerifyEmailPage(model: RegisterEmailResultModel) {
        this.codeFragment?.setModel(model)
        toPage(2)
    }

    override fun gotoEndPage() {
        footer?.visibility = View.INVISIBLE
        toPage(3)
        firebaseAnalyticsHelper?.logTutorialCompleteEvent()
    }

    private fun toPage(page: Int = 0) {
        viewPager?.setCurrentItem(page, true)
    }

    private fun initViewPager() {
        findViewById<ScrollControllViewPager>(R.id.view_pager).let {
            viewPager = it
            it.scrollDirection = ScrollControllViewPager.SCROLL_NONE
            val fragmentManager = getSupportFragmentManager()
            val adapter = TutorialViewPagerAdapter(fragmentManager)
            val firstFragment = TutorialFirstFragment.getInstance()
            firstFragment.startButtonClickHandler = object : View.OnClickListener {
                override fun onClick(v: View?) {
                    tutorialPresenter.start()
                }
            }
            adapter.addFragment(firstFragment)

            emailFragment = TutorialEmailFragment.getInstance()
            emailFragment?.let { fragment ->
                fragment.submitButtonClickHandler = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        tutorialPresenter.submitEmailAddress(fragment.getEmailAddressText())
                    }
                }
                adapter.addFragment(fragment)
            }

            codeFragment = TutorialCodeFragment.getInstance()
            codeFragment?.let { fragment ->
                fragment.submitButtonClickHandler = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        tutorialPresenter.verifyEmailAddress(fragment.getVerificationCode(), fragment.getPasswordOnImport())
                    }
                }
                adapter.addFragment(fragment)
            }

            endFragment = TutorialEndFragment.getInstance()
            endFragment?.let { fragment ->
                fragment.appStartButtonClickHandler = object : View.OnClickListener {
                    override fun onClick(v: View?) {
                        val intent = Intent(this@TutorialActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                adapter.addFragment(fragment)
            }

            it.adapter = adapter
            it.addOnPageChangeListener(this)
        }
    }

    private fun setupIndicator() {
        findViewById<View>(R.id.view_floating_indicator).let {
            floatingIndicatorView = it
        }
        findViewById<RelativeLayout>(R.id.layout_footer).let {
            footer = it
            footer?.visibility = View.VISIBLE
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
