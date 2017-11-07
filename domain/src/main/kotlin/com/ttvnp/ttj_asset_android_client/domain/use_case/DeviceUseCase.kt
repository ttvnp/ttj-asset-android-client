package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.exceptions.DeviceRegisterFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import io.reactivex.Observable
import javax.inject.Inject

interface DeviceUseCase {

    // initialize device info & register to service
    fun init() : Observable<DeviceModel>

    // get device info
    fun get() : Observable<DeviceModel>

    // register user by email address
    fun registerUserByEmail(emailAddress : String) : DeviceModel

    // verify email address and activate user
    fun activateUser(key : String, verificationCode : String) : UserModel

}

class DeviceUseCaseImpl @Inject constructor(
        private val repository: DeviceRepository
) : DeviceUseCase {

    override fun init(): Observable<DeviceModel> {
        return repository.register()
    }

    override fun get(): Observable<DeviceModel> {
        return repository.get()
    }

    override fun registerUserByEmail(emailAddress: String): DeviceModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun activateUser(key: String, verificationCode: String): UserModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
