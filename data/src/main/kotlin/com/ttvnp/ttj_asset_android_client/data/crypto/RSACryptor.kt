package com.ttvnp.ttj_asset_android_client.data.crypto

import android.annotation.TargetApi
import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.io.IOException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.InvalidAlgorithmParameterException
import java.security.KeyStore
import java.security.UnrecoverableKeyException
import java.security.InvalidKeyException
import java.security.Key
import java.security.PrivateKey
import java.security.KeyPairGenerator
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException

@TargetApi(Build.VERSION_CODES.M)
class RSACryptor(
        alias: String,
        blockMode: BlockMode,
        encryptionPadding: EncryptionPadding
) : BaseCryptor(
        alias = alias,
        blockMode = blockMode,
        encryptionPadding = encryptionPadding
) {

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    override fun createKeyStore(): KeyStore {
        return KeyStore.getInstance("AndroidKeyStore")
    }

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    override fun createCipher(blockMode: BlockMode, encryptionPadding: EncryptionPadding): Cipher {
        return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_RSA + "/" + blockMode.rawValue + "/" + encryptionPadding.rawValue)
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    override fun getEncryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getCertificate(alias).publicKey
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    override fun getDecryptKey(keyStore: KeyStore, alias: String): Key {
        return keyStore.getKey(alias, null) as PrivateKey
    }

    @Throws(NoSuchProviderException::class, NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class)
    override fun createNewKey(alias: String, blockMode: BlockMode, encryptionPadding: EncryptionPadding) {
        val generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore")
        generator.initialize(KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(blockMode.rawValue)
                .setEncryptionPaddings(encryptionPadding.rawValue)
                .build())
        generator.generateKeyPair()
    }

}