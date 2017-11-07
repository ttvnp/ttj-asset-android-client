package com.ttvnp.ttj_asset_android_client.data.service

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun register_isCorrect() {
        val service = DeviceServiceImpl()
        val deviceCode = "test-device-code012345678901234567890123456789=="
        val credential = "test-credential-012345678901234567890123456789=="
        val response = service.register(deviceCode, credential).doOnNext {
            assertEquals(0, it.exitCode)
            assertEquals("", it.message)
            assertNotEquals("", it.accessToken)
            assertNotNull(it.accessTokenExpiry)
        }
    }
}