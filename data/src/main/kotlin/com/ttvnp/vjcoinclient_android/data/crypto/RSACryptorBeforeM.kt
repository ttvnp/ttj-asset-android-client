package com.ttvnp.ttj_asset_android_client.data.crypto

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.security.KeyPairGeneratorSpec
import java.io.IOException
import java.math.BigInteger
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
import java.util.*
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.security.auth.x500.X500Principal

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
class RSACryptorBeforeM(
        alias: String,
        blockMode: BlockMode,
        encryptionPadding: EncryptionPadding,
        private val context: Context
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
        return Cipher.getInstance(CipherAlgorithm.RSA.rawValue + "/" + blockMode.rawValue + "/" + encryptionPadding.rawValue)
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
        val start = Calendar.getInstance()
        val end = Calendar.getInstance()
        end.add(Calendar.YEAR, 100)
        val generator = KeyPairGenerator.getInstance(CipherAlgorithm.RSA.rawValue, "AndroidKeyStore")
        generator.initialize(KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSubject(X500Principal("CN=Cryptore"))
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(start.time)
                .setEndDate(end.time)
                .build())
        generator.generateKeyPair()
    }

}