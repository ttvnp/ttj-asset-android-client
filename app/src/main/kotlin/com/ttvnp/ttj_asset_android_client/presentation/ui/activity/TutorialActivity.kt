package com.ttvnp.ttj_asset_android_client.presentation.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.View
import com.ttvnp.ttj_asset_android_client.presentation.R
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
    lateinit var tutorialPresenter: TutorialPresenter

    private var emailFragment: TutorialEmailFragment? = null
    private var codeFragment: TutorialCodeFragment? = null
    private var endFragment: TutorialEndFragment? = null
    private var viewPager: ScrollControllViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        initViewPager()
        tutorialPresenter.onCreate(this)
    }

    override fun showError(errorCode: ErrorCode, throwable: Throwable?) {
        val errMsg = errorMessageGenerator.generate(errorCode, throwable)
        when (errorCode) {
            ErrorCode.ERROR_VALIDATION_EMAIL -> emailFragment?.showValidationError(errMsg)
            ErrorCode.ERROR_VALIDATION_VERIFICATION_CODE -> codeFragment?.showCodeValidationError(errMsg)
            ErrorCode.ERROR_VALIDATION_PASSWORD_ON_IMPORT -> codeFragment?.showPasswordValidationError(errMsg)
            else -> showErrorDialog(errMsg)
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

                val dialogInterface = { dialogInterface: DialogInterface, i: Int ->
                    dialogInterface.dismiss()
                }

                fragment.termsAndConditionsClickHandler = View.OnClickListener {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle(null)
                    dialog.setMessage(getString(R.string.terms_and_conditions_description))
                    dialog.setPositiveButton(getString(R.string.close), dialogInterface)
                    dialog.show()
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

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) {
    }
}
