package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.isEmailValid
import io.reactivex.Observable
import javax.inject.Inject

interface DeviceUseCase {

    // initialize device info & register to service
    fun init() : Observable<DeviceModel>

    // register user by email address
    fun registerEmail(emailAddress : String) : Observable<DeviceModel>

    // verify email address and activate user
    fun verifyEmail(verificationCode : String) : Observable<UserModel>

}

class DeviceUseCaseImpl @Inject constructor(
        private val repository: DeviceRepository
) : DeviceUseCase {

    override fun init(): Observable<DeviceModel> {
        return repository.register()
    }

    override fun registerEmail(emailAddress: String): Observable<DeviceModel> {
        val input = emailAddress.trim()
        if (!isEmailValid(input)) {
            throw ValidationException("emailAddress")
        }
        return repository.registerEmail(input)
    }

    override fun verifyEmail(verificationCode: String): Observable<UserModel> {
        val input = verificationCode.trim()
        return repository.verifyEmail(input)
    }
}
