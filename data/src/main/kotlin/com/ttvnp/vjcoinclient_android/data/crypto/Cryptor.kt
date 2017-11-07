package com.ttvnp.ttj_asset_android_client.data.crypto

import java.io.IOException
import java.security.*
import javax.crypto.NoSuchPaddingException

interface Cryptor {

    @Throws(KeyStoreException::class, NoSuchPaddingException::class, NoSuchAlgorithmException::class, InvalidKeyException::class, IOException::class, NoSuchProviderException::class, InvalidAlgorithmParameterException::class, UnrecoverableEntryException::class)
    fun encrypt(plainByte: ByteArray): EncryptResult

    @Throws(UnrecoverableKeyException::class, NoSuchAlgorithmException::class, KeyStoreException::class, InvalidAlgorithmParameterException::class, InvalidKeyException::class, IOException::class)
    fun decrypt(encryptedByte: ByteArray, cipherIV: ByteArray? = null): DecryptResult
}
