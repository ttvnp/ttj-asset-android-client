package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.exceptions.ValidationException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.isEmailValid
import io.reactivex.Single
import javax.inject.Inject

interface DeviceUseCase {

    // get device
    fun getDevice(): Single<ModelWrapper<DeviceModel?>>

    // initialize device info & register to service
    fun init() : Single<DeviceModel>

    // register user by email address
    fun registerEmail(emailAddress : String) : Single<DeviceModel>

    // verify email address and activate user
    fun verifyEmail(verificationCode : String) : Single<UserModel>

}

class DeviceUseCaseImpl @Inject constructor(
        private val repository: DeviceRepository
) : DeviceUseCase {

    override fun getDevice(): Single<ModelWrapper<DeviceModel?>> {
        return repository.getDevice()
    }

    override fun init(): Single<DeviceModel> {
        return repository.register()
    }

    override fun registerEmail(emailAddress: String): Single<DeviceModel> {
        val input = emailAddress.trim()
        if (!isEmailValid(input)) {
            throw ValidationException("emailAddress")
        }
        return repository.registerEmail(input)
    }

    override fun verifyEmail(verificationCode: String): Single<UserModel> {
        val input = verificationCode.trim()
        return repository.verifyEmail(input)
    }
}
