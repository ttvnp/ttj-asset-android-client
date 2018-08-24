package com.ttvnp.ttj_asset_android_client.presentation.ui.error

import android.content.Context
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.presentation.R
import javax.inject.Inject

class ErrorMessageGenerator @Inject constructor(val context: Context) {

    fun default(): String {
        return context.getString(R.string.error_default_message)
    }

    fun generate(errorCode: ErrorCode, throwable: Throwable?): String {
        return context.getString(convert(errorCode))
    }

    fun convert(errorCode: ErrorCode): Int {
        return when (errorCode) {
            ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER -> R.string.error_cannot_connect_to_server
            ErrorCode.ERROR_LOCKED_OUT -> R.string.error_lockout_message
            ErrorCode.ERROR_VALIDATION_EMAIL -> R.string.error_validation_email_address
            ErrorCode.ERROR_VALIDATION_VERIFICATION_CODE -> R.string.error_invalid_verification_code
            ErrorCode.ERROR_VALIDATION_PASSWORD_ON_IMPORT -> R.string.error_invalid_password_on_import
            ErrorCode.ERROR_DEVICE_ALREADY_SETUP -> R.string.error_device_already_setup
            ErrorCode.ERROR_UPLOAD_PROFILE_IMAGE_FILE_TOO_LARGE -> R.string.error_cannot_update_profile_image
            ErrorCode.ERROR_CANNOT_SELF_SEND -> R.string.error_cannot_self_send
            ErrorCode.ERROR_VALIDATION_TOO_MUCH_AMOUNT -> R.string.error_cannot_send_that_amount
            ErrorCode.ERROR_CANNOT_FIND_TARGET_USER -> R.string.error_message_no_such_email_address_user
            ErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT -> R.string.old_password_is_not_correct
            else -> R.string.error_default_message
        }
    }
}

