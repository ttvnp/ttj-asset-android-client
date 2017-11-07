package com.ttvnp.ttj_asset_android_client.data.crypto

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.cert.CertificateException
import javax.crypto.NoSuchPaddingException

class CryptorFactory(
        var alias: String,
        var type: CipherAlgorithm = CryptorFactory.CIPHER_ALGORITHM_DEFAULT
) {

    companion object {
        private val CIPHER_ALGORITHM_DEFAULT = CipherAlgorithm.RSA
        private val BLOCK_MODE_DEFAULT__AES = BlockMode.CBC
        private val ENCRYPTION_PADDING_DEFAULT__AES = EncryptionPadding.PKCS7
        private val BLOCK_MODE_DEFAULT__RSA = BlockMode.ECB
        private val ENCRYPTION_PADDING_DEFAULT__RSA = EncryptionPadding.RSA_PKCS1
    }

    var context: Context? = null
    var blockMode: BlockMode
    var encryptionPadding: EncryptionPadding

    init {
        when (type) {
            CipherAlgorithm.RSA -> {
                blockMode = BLOCK_MODE_DEFAULT__RSA
                encryptionPadding = ENCRYPTION_PADDING_DEFAULT__RSA
            }
            CipherAlgorithm.AES -> {
                blockMode = BLOCK_MODE_DEFAULT__AES
                encryptionPadding = ENCRYPTION_PADDING_DEFAULT__AES
            }
        }
    }

    @Throws(CertificateException::class, NoSuchAlgorithmException::class, KeyStoreException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class, IOException::class, NoSuchPaddingException::class)
    fun createInstance(): Cryptor {
        when (type) {
            CipherAlgorithm.AES -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return AESCryptor(alias = alias, blockMode = blockMode, encryptionPadding = encryptionPadding)
                } else {
                    throw NoSuchAlgorithmException("AES is support only above API Lv23.")
                }
            }
            CipherAlgorithm.RSA -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    context?.let { Log.i("Cryptore", "No Need \"Context\" for RSA on above API Lv23") }
                    return RSACryptor(alias = alias, blockMode = blockMode, encryptionPadding = encryptionPadding)
                } else if (Build.VERSION_CODES.JELLY_BEAN_MR2 <= Build.VERSION.SDK_INT) {
                    context?.let {
                        return RSACryptorBeforeM(alias = alias, blockMode = blockMode, encryptionPadding = encryptionPadding, context = it)
                    } ?: run {
                        throw NullPointerException("Need \"Context\" for RSA on below API Lv22")
                    }
                } else {
                    throw NoSuchAlgorithmException("RSA is support only above API Lv18.")
                }
            }
            else -> throw IllegalArgumentException("Unsupported Algorithm.")
        }
    }
}