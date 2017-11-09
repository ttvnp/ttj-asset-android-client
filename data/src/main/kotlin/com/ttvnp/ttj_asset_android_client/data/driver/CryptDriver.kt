package com.ttvnp.ttj_asset_android_client.data.driver

import android.util.Base64
import com.ttvnp.ttj_asset_android_client.data.crypto.Cryptor
import javax.inject.Inject

interface CryptDriver {
    fun encrypt(plainText: String): String?
    fun decrypt(encryptedString: String): String?
}

class CryptDriverImpl @Inject constructor(val cryptor: Cryptor) : CryptDriver {

    override fun encrypt(plainText: String): String? {
        val byteArray = plainText.toByteArray()
        val result = cryptor.encrypt(byteArray)
        return Base64.encodeToString(result.bytes, Base64.DEFAULT)
    }

    override fun decrypt(encryptedString: String): String? {
        val byteArray = Base64.decode(encryptedString, Base64.DEFAULT)
        val result = cryptor.decrypt(byteArray, null)
        return String(result.bytes)
    }
}
