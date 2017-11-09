package com.ttvnp.ttj_asset_android_client.data.crypto

import java.io.IOException
import java.security.NoSuchAlgorithmException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.Key
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchProviderException
import java.security.UnrecoverableKeyException
import java.security.cert.CertificateException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.IvParameterSpec

abstract class BaseCryptor(
        private val alias: String,
        blockMode: BlockMode,
        encryptionPadding: EncryptionPadding
) : Cryptor {

    private val cipher: Cipher
    private val keyStore: KeyStore

    init {
        cipher = this.createCipher(blockMode = blockMode, encryptionPadding = encryptionPadding)
        keyStore = this.createKeyStore()
        keyStore.load(null)
        if (!keyStore.containsAlias(alias)) this.createNewKey(alias = alias, blockMode = blockMode, encryptionPadding = encryptionPadding)
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    override fun encrypt(plainByte: ByteArray): EncryptResult {
        val encryptKey = getEncryptKey(keyStore = keyStore, alias = alias)
        cipher.init(Cipher.ENCRYPT_MODE, encryptKey)
        return EncryptResult(cipher.doFinal(plainByte), cipher.iv)
    }

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    override fun decrypt(encryptedByte: ByteArray, cipherIV: ByteArray?): DecryptResult {
        val decryptKey = getDecryptKey(keyStore = keyStore, alias = alias)
        cipherIV?.let {
            cipher.init(Cipher.DECRYPT_MODE, decryptKey, IvParameterSpec(cipherIV))
        } ?: run {
            cipher.init(Cipher.DECRYPT_MODE, decryptKey)
        }
        return DecryptResult(cipher.doFinal(encryptedByte), cipher.iv)
    }

    @Throws(KeyStoreException::class, CertificateException::class, NoSuchAlgorithmException::class, IOException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class)
    protected abstract fun createKeyStore(): KeyStore

    @Throws(NoSuchPaddingException::class, NoSuchAlgorithmException::class)
    protected abstract fun createCipher(blockMode: BlockMode, encryptionPadding: EncryptionPadding): Cipher

    @Throws(NoSuchAlgorithmException::class, InvalidAlgorithmParameterException::class, NoSuchProviderException::class, KeyStoreException::class)
    protected abstract fun createNewKey(alias: String, blockMode: BlockMode, encryptionPadding: EncryptionPadding)

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidKeyException::class, IOException::class)
    protected abstract fun getEncryptKey(keyStore: KeyStore, alias: String): Key

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    protected abstract fun getDecryptKey(keyStore: KeyStore, alias: String): Key

}
