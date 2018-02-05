package com.ttvnp.ttj_asset_android_client.data.crypto

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.InvalidKeyException
import java.security.InvalidAlgorithmParameterException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.NoSuchPaddingException
import javax.crypto.SecretKey

@TargetApi(Build.VERSION_CODES.M)
class AESCryptor(
        alias: String,
        blockMode: BlockMode,
        encryptionPadding: EncryptionPadding,
        context: Context?
) : BaseCryptor(
        alias = alias,
        blockMode = blockMode,
        encryptionPadding = encryptionPadding,
        context = context
) {

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    override fun createKeyStore(): KeyStore {
        return KeyStore.getInstance("AndroidKeyStore")
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    override fun createCipher(blockMode: BlockMode, encryptionPadding: EncryptionPadding): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" + blockMode.rawValue + "/" + encryptionPadding.rawValue)
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    override fun getEncryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as SecretKey
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    override fun getDecryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as SecretKey
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
    override fun createNewKey(context: Context?, alias: String, blockMode: BlockMode, encryptionPadding: EncryptionPadding) {
        val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        generator.init(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(blockMode.rawValue)
                .setEncryptionPaddings(encryptionPadding.rawValue)
                .build())
        generator.generateKey()
    }
}