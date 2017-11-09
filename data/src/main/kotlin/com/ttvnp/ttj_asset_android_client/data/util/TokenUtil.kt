package com.ttvnp.ttj_asset_android_client.data.util

import android.util.Base64
import java.security.SecureRandom

class TokenUtil {
    companion object {
        fun generateToken68(len: Int): String {
            val randomBytes: ByteArray = ByteArray(len)
            SecureRandom().nextBytes(randomBytes)
            return Base64
                    .encodeToString(randomBytes, Base64.DEFAULT)
                    .replace("\n", "")
        }
    }
}
