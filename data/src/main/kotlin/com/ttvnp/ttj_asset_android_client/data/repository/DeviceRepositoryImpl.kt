package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.service.DeviceServiceWithNoAuth
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceResponse
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.DeviceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.data.util.TokenUtil
import com.ttvnp.ttj_asset_android_client.domain.exceptions.ServiceFailedException
import com.ttvnp.ttj_asset_android_client.domain.model.DeviceModel
import com.ttvnp.ttj_asset_android_client.domain.model.ErrorCode
import com.ttvnp.ttj_asset_android_client.domain.model.ModelWrapper
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import java.io.IOException
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceServiceWithNoAuth : DeviceServiceWithNoAuth,
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore,
        private val userDataStore : UserDataStore
) : DeviceRepository {

    override fun getDevice(): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            val deviceInfo = deviceInfoDataStore.get()
            if (deviceInfo == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                return@create
            }

            // get from device info
            var deviceEntity: DeviceEntity? = null
            deviceEntity = deviceDataStore.get() // at first from local.
            if (deviceEntity != null && deviceEntity.isActivated) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                return@create
            }

            val response: DeviceResponse
            try {
                response = deviceService.get().execute().body()!!
                val serviceResult: ModelWrapper<DeviceModel?>
                if (response.hasError()) {
                    serviceResult = ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED)
                } else {
                    var de = DeviceEntity(
                            accessToken = response.accessToken,
                            accessTokenExpiry = response.accessTokenExpiry,
                            isActivated = response.isActivated,
                            deviceToken = response.deviceToken,
                            grantPushNotification = response.grantPushNotification,
                            grantEmailNotification = response.grantEmailNotification
                    )
                    de = deviceDataStore.update(de)
                    serviceResult = ModelWrapper<DeviceModel?>(DeviceTranslator().translate(de), ErrorCode.NO_ERROR)
                }
                subscriber.onSuccess(serviceResult)
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun register(): Single<ModelWrapper<DeviceModel?>> {
        // call api service to register this device and retrieve access token as well.

        val register: (SingleEmitter<ModelWrapper<DeviceModel?>>) -> Unit = { subscriber ->
            // newly create device code and credential.
            val initDeviceCode = TokenUtil.generateToken68(64)
            val initCredential = TokenUtil.generateToken68(64)
            val response: DeviceResponse
            try {
                response = deviceServiceWithNoAuth.register(initDeviceCode, initCredential).execute().body()!!
                if (response.hasError()) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_REGISTER_DEVICE))
                } else {
                    deviceInfoDataStore.save(DeviceInfoEntity(initDeviceCode, initCredential))
                    var deviceEntity = DeviceEntity(
                            accessToken = response.accessToken,
                            accessTokenExpiry = response.accessTokenExpiry,
                            isActivated = response.isActivated,
                            deviceToken = response.deviceToken,
                            grantPushNotification = response.grantPushNotification,
                            grantEmailNotification = response.grantEmailNotification
                    )
                    deviceEntity = deviceDataStore.update(deviceEntity)
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity)!!, ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }

        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            val deviceInfo = deviceInfoDataStore.get()
            if (deviceInfo == null) {
                register(subscriber)
                return@create
            }

            val deviceEntity = deviceDataStore.get()
            if (deviceEntity != null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity)!!, ErrorCode.NO_ERROR))
                return@create
            }

            val responseBody = deviceService.get().execute()?.body()
            if (responseBody == null) {
                register(subscriber)
            } else {
                if (responseBody.hasError()) {
                    register(subscriber)
                }
                var deviceEntity2 = DeviceEntity(
                        accessToken = responseBody.accessToken,
                        accessTokenExpiry = responseBody.accessTokenExpiry,
                        isActivated = responseBody.isActivated,
                        deviceToken = responseBody.deviceToken,
                        grantPushNotification = responseBody.grantPushNotification,
                        grantEmailNotification = responseBody.grantEmailNotification
                )
                deviceEntity2 = deviceDataStore.update(deviceEntity2)
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity2), ErrorCode.NO_ERROR))
            }
        }
    }

    override fun registerEmail(emailAddress: String): Single<DeviceModel> {
        return deviceService.registerEmail(emailAddress).map {
            if (it.hasError()) {
                throw ServiceFailedException()
            }
            val deviceEntity = deviceDataStore.get()
            DeviceTranslator().translate(deviceEntity)!!
        }
    }

    override fun verifyEmail(verificationCode: String): Single<UserModel> {
        return deviceService.verifyEmail(verificationCode).map { response ->
            if (response.hasError()) {
                throw ServiceFailedException()
            }
            var userEntity = UserEntity(
                    emailAddress = response.emailAddress,
                    profileImageID = response.profileImageID,
                    profileImageURL = response.profileImageURL,
                    firstName = response.firstName,
                    middleName =  response.middleName,
                    lastName = response.lastName,
                    address = response.address,
                    isEmailVerified = response.isEmailVerified,
                    isIdentified = response.isIdentified
            )
            userEntity = userDataStore.update(userEntity)

            // update device entity
            deviceDataStore.get()?.let {
                val newDeviceEntity = DeviceEntity(
                        accessToken = it.accessToken,
                        accessTokenExpiry = it.accessTokenExpiry,
                        isActivated = true,
                        deviceToken = it.deviceToken,
                        grantPushNotification = it.grantPushNotification,
                        grantEmailNotification = it.grantEmailNotification
                )
                deviceDataStore.update(newDeviceEntity)
            }

            UserTranslator().translate(userEntity)!!
        }
    }

    override fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                return@create
            }
            val disposables = CompositeDisposable()
            deviceService.updateDeviceToken(deviceToken).subscribeWith(object : DisposableSingleObserver<DeviceResponse>() {
                override fun onSuccess(response: DeviceResponse) {
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                        return
                    }
                    deviceEntity = DeviceEntity(
                            accessToken = response.accessToken,
                            accessTokenExpiry = response.accessTokenExpiry,
                            isActivated = response.isActivated,
                            deviceToken = response.deviceToken,
                            grantPushNotification = response.grantPushNotification,
                            grantEmailNotification = response.grantEmailNotification
                    )
                    deviceEntity = deviceDataStore.update(deviceEntity!!)
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                }
                override fun onError(e: Throwable) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                }
            }).addTo(disposables)
        }
    }

    override fun updateNotificationSettings(grantPushNotification: Boolean?, grantEmailNotification: Boolean?): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                return@create
            }
            val disposables = CompositeDisposable()
            val grantPushNotificationUpdateValue = grantPushNotification?:deviceEntity.grantPushNotification
            val grantEmailNotificationUpdateValue = grantEmailNotification?:deviceEntity.grantEmailNotification
            deviceService.updateNotificationSettings(grantPushNotificationUpdateValue, grantEmailNotificationUpdateValue).subscribeWith(object : DisposableSingleObserver<DeviceResponse>() {
                override fun onSuccess(response: DeviceResponse) {
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                        return
                    }
                    deviceEntity = DeviceEntity(
                            accessToken = response.accessToken,
                            accessTokenExpiry = response.accessTokenExpiry,
                            isActivated = response.isActivated,
                            deviceToken = response.deviceToken,
                            grantPushNotification = response.grantPushNotification,
                            grantEmailNotification = response.grantEmailNotification
                    )
                    deviceEntity = deviceDataStore.update(deviceEntity!!)
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                }
                override fun onError(e: Throwable) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                }
            }).addTo(disposables)
        }
    }
}