package com.ttvnp.ttj_asset_android_client.data.util

import android.util.Base64
import com.ttvnp.ttj_asset_android_client.data.BuildConfig
import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

class ServerCryptoUtil {
    companion object {

        private val cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
        init {
            val publicKeyData = BuildConfig.SERVER_PUB_KEY
            val keyFactory = KeyFactory.getInstance("RSA")
            val keyBytes = readPem(publicKeyData)
            val keySpec = X509EncodedKeySpec(keyBytes)
            val publicKey = keyFactory.generatePublic(keySpec)
            cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        }

        fun encrypt(plainText: String): String {
            val encryptedData = cipher!!.doFinal(plainText.toByteArray())
            val sb = StringBuilder()
            for (b in encryptedData) {
                sb.append(String.format("%02X", b))
            }
            return sb.toString()
        }

        private fun readPem(keyString: String): ByteArray {
            val str = keyString.replace("-----BEGIN PUBLIC KEY-----\n", "")
                    .replace("-----END PUBLIC KEY-----", "")
            return Base64.decode(str, Base64.DEFAULT)
        }
    }
}