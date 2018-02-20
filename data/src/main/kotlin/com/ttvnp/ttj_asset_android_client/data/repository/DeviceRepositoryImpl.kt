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
import retrofit2.HttpException
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
            val deviceEntity: DeviceEntity? = deviceDataStore.get()
            if (deviceEntity != null && deviceEntity.isActivated) {
                subscriber.onSuccess(ModelWrapper<DeviceModel?>(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                return@create
            }

            try {
                val getResponse = deviceService.get().execute()
                if (!getResponse.isSuccessful) {
                    subscriber.onError(HttpException(getResponse))
                    return@create
                }
                getResponse.body()?.let {
                    val response: DeviceResponse = it
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_DEVICE_NOT_REGISTERED))
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
                    subscriber.onSuccess(ModelWrapper(DeviceTranslator().translate(de), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
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
                    try {
                        val registerResponse = deviceServiceWithNoAuth.register(initDeviceCode, initCredential, recaptchaToken).execute()
                        if (!registerResponse.isSuccessful) {
                            subscriber.onError(HttpException(registerResponse))
                            return@create
                        }
                        registerResponse.body()?.let {
                            val response: DeviceResponse = it
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
                        subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN, e))
                    }
                }).addTo(disposables)
            }.addOnFailureListener{ e ->
                if (e is ApiException) {
                    val errCode: ErrorCode = when (e.statusCode) {
                        SafetyNetStatusCodes.API_NOT_CONNECTED -> ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
                        SafetyNetStatusCodes.TIMEOUT -> ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
                        else -> ErrorCode.ERROR_UNKNOWN
                    }
                    subscriber.onSuccess(ModelWrapper(null, errCode, e))
                } else {
                    subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN, e))
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
                subscriber.onSuccess(ModelWrapper(DeviceTranslator().translate(deviceEntity)!!, ErrorCode.NO_ERROR))
                return@create
            }


            val getResponse = deviceService.get().execute()
            if (!getResponse.isSuccessful) {
                subscriber.onError(HttpException(getResponse))
                return@create
            }
            val responseBody = getResponse.body()
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
                subscriber.onSuccess(ModelWrapper(DeviceTranslator().translate(deviceEntity2), ErrorCode.NO_ERROR))
            }
        }
    }

    override fun registerEmail(emailAddress: String): Single<ModelWrapper<RegisterEmailResultModel?>> {
        return Single.create<ModelWrapper<RegisterEmailResultModel?>> { subscriber ->
            try {
                val registerEmailResponse = deviceService.registerEmail(emailAddress).execute()
                if (!registerEmailResponse.isSuccessful) {
                    subscriber.onError(HttpException(registerEmailResponse))
                    return@create
                }
                registerEmailResponse.body()?.let {
                    val response: DeviceRegisterEmailResponse = registerEmailResponse.body()!!
                    if (response.hasError()) {
                        val errorCode: ErrorCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_INVALID_EMAIL_ADDRESS_FORMAT.rawValue -> ErrorCode.ERROR_VALIDATION_EMAIL
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        subscriber.onSuccess(ModelWrapper(null, errorCode))
                        return@create
                    }
                    val model = RegisterEmailResultModel(response.isEmailInUse)
                    subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun verifyEmail(verificationCode: String, passwordOnImport: String): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            try {
                val verifyEmailResponse = deviceService.verifyEmail(verificationCode, passwordOnImport).execute()
                if (!verifyEmailResponse.isSuccessful) {
                    subscriber.onError(HttpException(verifyEmailResponse))
                    return@create
                }
                verifyEmailResponse.body()?.let {
                    val response: DeviceVerifyEmailResponse = verifyEmailResponse.body()!!
                    if (response.hasError()) {
                        val errorCode: ErrorCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_INVALID_VERIFICATION_CODE.rawValue -> ErrorCode.ERROR_VALIDATION_VERIFICATION_CODE
                            ServiceErrorCode.ERROR_INVALID_PASSWORD_ON_IMPORT.rawValue -> ErrorCode.ERROR_VALIDATION_PASSWORD_ON_IMPORT
                            ServiceErrorCode.ERROR_ILLEGAL_DATA_STATE.rawValue -> ErrorCode.ERROR_DEVICE_ALREADY_SETUP
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        subscriber.onSuccess(ModelWrapper(null, errorCode))
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
                    subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun updateDeviceToken(deviceToken: String): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            try {
                val updateDeviceTokenResponse = deviceService.updateDeviceToken(deviceToken).execute()
                if (!updateDeviceTokenResponse.isSuccessful) {
                    subscriber.onError(HttpException(updateDeviceTokenResponse))
                    return@create
                }
                updateDeviceTokenResponse.body()?.let {
                    val response = it
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
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
                    deviceEntity = deviceDataStore.update(deviceEntity!!)
                    subscriber.onSuccess(ModelWrapper(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun updateNotificationSettings(grantPushNotification: Boolean?, grantEmailNotification: Boolean?): Single<ModelWrapper<DeviceModel?>> {
        return Single.create<ModelWrapper<DeviceModel?>> { subscriber ->
            var deviceEntity = deviceDataStore.get()
            if (deviceEntity == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val grantPushNotificationUpdateValue = grantPushNotification?:deviceEntity.grantPushNotification
            val grantEmailNotificationUpdateValue = grantEmailNotification?:deviceEntity.grantEmailNotification

            try {
                val updateNotificationSettings = deviceService.updateNotificationSettings(grantPushNotificationUpdateValue, grantEmailNotificationUpdateValue).execute()
                if (!updateNotificationSettings.isSuccessful) {
                    subscriber.onError(HttpException(updateNotificationSettings))
                    return@create
                }
                updateNotificationSettings.body()?.let {
                    val response = it
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
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
                    deviceEntity = deviceDataStore.update(deviceEntity!!)
                    subscriber.onSuccess(ModelWrapper(DeviceTranslator().translate(deviceEntity), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }
}