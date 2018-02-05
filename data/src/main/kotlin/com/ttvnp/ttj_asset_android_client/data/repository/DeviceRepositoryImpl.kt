package com.ttvnp.ttj_asset_android_client.data.repository

import com.google.android.gms.common.api.ApiException
import com.google.android.gms.safetynet.SafetyNetStatusCodes
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceEntity
import com.ttvnp.ttj_asset_android_client.data.entity.DeviceInfoEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.service.DeviceServiceWithNoAuth
import com.ttvnp.ttj_asset_android_client.data.service.RecaptchaService
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceRegisterEmailResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.DeviceVerifyEmailResponse
import com.ttvnp.ttj_asset_android_client.data.service.response.ServiceErrorCode
import com.ttvnp.ttj_asset_android_client.data.store.DeviceDataStore
import com.ttvnp.ttj_asset_android_client.data.store.DeviceInfoDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.DeviceTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.data.util.TokenUtil
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import javax.inject.Inject

class DeviceRepositoryImpl @Inject constructor(
        private val deviceServiceWithNoAuth : DeviceServiceWithNoAuth,
        private val deviceService : DeviceService,
        private val deviceDataStore : DeviceDataStore,
        private val deviceInfoDataStore : DeviceInfoDataStore,
        private val userDataStore : UserDataStore,
        private val recaptchaService: RecaptchaService
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
                if (response.hasError()) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
                    return@create
                }
                var de = DeviceEntity(
                        accessToken = response.accessToken,
                        accessTokenExpiry = response.accessTokenExpiry,
                        isActivated = response.isActivated,
                        deviceToken = response.deviceToken,
                        grantPushNotification = response.grantPushNotification,
                        grantEmailNotification = response.grantEmailNotification
                )
                de = deviceDataStore.update(de)
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(de), ErrorCode.NO_ERROR))
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun register(): Single<ModelWrapper<DeviceModel?>> {

        val register: (SingleEmitter<ModelWrapper<DeviceModel?>>) -> Unit = { subscriber ->

            recaptchaService.verifyWithRecaptcha().addOnSuccessListener { recaptchaTokenResponse ->
                val recaptchaToken = recaptchaTokenResponse.tokenResult
                // note that this is in the main thread.
                val disposables = CompositeDisposable()
                Single.create<ModelWrapper<DeviceModel?>> { inner ->
                    // newly create device code and credential.
                    val initDeviceCode = TokenUtil.generateToken68(64)
                    val initCredential = TokenUtil.generateToken68(64)
                    val response: DeviceResponse
                    try {
                        response = deviceServiceWithNoAuth.register(initDeviceCode, initCredential, recaptchaToken).execute().body()!!
                        if (response.hasError()) {
                            inner.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_REGISTER_DEVICE))
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
                            inner.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity)!!, ErrorCode.NO_ERROR))
                        }
                    } catch (e: IOException) {
                        inner.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
                    }
                }.subscribeOn(Schedulers.io())
                .subscribeWith(object : DisposableSingleObserver<ModelWrapper<DeviceModel?>>() {
                    override fun onSuccess(wrapper: ModelWrapper<DeviceModel?>) {
                        subscriber.onSuccess(wrapper)
                    }
                    override fun onError(e: Throwable) {
                        subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_UNKNOWN, e))
                    }
                }).addTo(disposables)
            }.addOnFailureListener{ e ->
                if (e is ApiException) {
                    val errCode: ErrorCode
                    when (e.statusCode) {
                        SafetyNetStatusCodes.API_NOT_CONNECTED -> errCode = ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
                        SafetyNetStatusCodes.TIMEOUT -> errCode = ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
                        else -> errCode = ErrorCode.ERROR_UNKNOWN
                    }
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, errCode, e))
                } else {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_UNKNOWN, e))
                }
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
                    return@create
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

    override fun registerEmail(emailAddress: String): Single<ModelWrapper<RegisterEmailResultModel?>> {
        return Single.create<ModelWrapper<RegisterEmailResultModel?>> { subscriber ->
            val response: DeviceRegisterEmailResponse
            try {
                response = deviceService.registerEmail(emailAddress).execute().body()!!
                if (response.hasError()) {
                    val errorCode: ErrorCode
                    when (response.errorCode) {
                        ServiceErrorCode.ERROR_INVALID_EMAIL_ADDRESS_FORMAT.rawValue -> errorCode = ErrorCode.ERROR_VALIDATION_EMAIL
                        else -> errorCode = ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                    }
                    subscriber.onSuccess(ModelWrapper<RegisterEmailResultModel?>(null, errorCode))
                    return@create
                }
                val model = RegisterEmailResultModel(response.isEmailInUse)
                subscriber.onSuccess(ModelWrapper<RegisterEmailResultModel?>(model, ErrorCode.NO_ERROR))
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<RegisterEmailResultModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun verifyEmail(verificationCode: String, passwordOnImport: String): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            val response: DeviceVerifyEmailResponse
            try {
                response = deviceService.verifyEmail(verificationCode, passwordOnImport).execute().body()!!
                if (response.hasError()) {
                    val errorCode: ErrorCode
                    when (response.errorCode) {
                        ServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE.rawValue -> errorCode = ErrorCode.ERROR_VALIDATION_VERIFICATION_CODE
                        ServiceErrorCode.ERROR_INVALID_PASSWORD_ON_IMPORT.rawValue -> errorCode = ErrorCode.ERROR_VALIDATION_PASSWORD_ON_IMPORT
                        ServiceErrorCode.ERROR_ILLEGAL_DATA_STATE.rawValue -> errorCode = ErrorCode.ERROR_DEVICE_ALREADY_SETUP
                        else -> errorCode = ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                    }
                    subscriber.onSuccess(ModelWrapper<UserModel?>(null, errorCode))
                    return@create
                }
                var userEntity = UserEntity(
                        emailAddress = response.user.emailAddress,
                        profileImageID = response.user.profileImageID,
                        profileImageURL = response.user.profileImageURL,
                        firstName = response.user.firstName,
                        middleName =  response.user.middleName,
                        lastName = response.user.lastName,
                        address = response.user.address,
                        isEmailVerified = response.user.isEmailVerified,
                        isIdentified = response.user.isIdentified,
                        updatedAt = Now()
                )
                userEntity = userDataStore.update(userEntity)

                // update device entity
                val deviceEntity = DeviceEntity(
                        accessToken = response.device.accessToken,
                        accessTokenExpiry = response.device.accessTokenExpiry,
                        isActivated = response.device.isActivated,
                        deviceToken = response.device.deviceToken,
                        grantPushNotification = response.device.grantPushNotification,
                        grantEmailNotification = response.device.grantEmailNotification
                )
                deviceDataStore.update(deviceEntity)

                val model = UserTranslator().translate(userEntity)!!
                subscriber.onSuccess(ModelWrapper<UserModel?>(model, ErrorCode.NO_ERROR))
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<UserModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            try {
                val response = deviceService.updateDeviceToken(deviceToken).execute().body()!!
                if (response.hasError()) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                    return@create
                }
                deviceEntity = DeviceEntity(
                        accessToken = response.accessToken,
                        accessTokenExpiry = response.accessTokenExpiry,
                        isActivated = response.isActivated,
                        deviceToken = response.deviceToken,
                        grantPushNotification = response.grantPushNotification,
                        grantEmailNotification = response.grantEmailNotification
                )
                deviceEntity = deviceDataStore.update(deviceEntity)
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun updateNotificationSettings(grantPushNotification: Boolean?, grantEmailNotification: Boolean?): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val disposables = CompositeDisposable()
            val grantPushNotificationUpdateValue = grantPushNotification?:deviceEntity.grantPushNotification
            val grantEmailNotificationUpdateValue = grantEmailNotification?:deviceEntity.grantEmailNotification

            try {
                val response = deviceService.updateNotificationSettings(grantPushNotificationUpdateValue, grantEmailNotificationUpdateValue).execute().body()!!
                if (response.hasError()) {
                    subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                    return@create
                }
                deviceEntity = DeviceEntity(
                        accessToken = response.accessToken,
                        accessTokenExpiry = response.accessTokenExpiry,
                        isActivated = response.isActivated,
                        deviceToken = response.deviceToken,
                        grantPushNotification = response.grantPushNotification,
                        grantEmailNotification = response.grantEmailNotification
                )
                deviceEntity = deviceDataStore.update(deviceEntity)
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }
}