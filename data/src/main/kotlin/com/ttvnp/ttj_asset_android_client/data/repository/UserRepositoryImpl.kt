package com.ttvnp.ttj_asset_android_client.data.repository

import com.ttvnp.ttj_asset_android_client.data.BuildConfig
import com.ttvnp.ttj_asset_android_client.data.entity.OtherUserEntity
import com.ttvnp.ttj_asset_android_client.data.entity.StellarAccountEntity
import com.ttvnp.ttj_asset_android_client.data.entity.UserEntity
import com.ttvnp.ttj_asset_android_client.data.service.StellarService
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.service.response.ServiceErrorCode
import com.ttvnp.ttj_asset_android_client.data.store.OtherUserDataStore
import com.ttvnp.ttj_asset_android_client.data.store.StellarAccountDataStore
import com.ttvnp.ttj_asset_android_client.data.store.UserDataStore
import com.ttvnp.ttj_asset_android_client.data.translator.OtherUserTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.StellarAccountTranslator
import com.ttvnp.ttj_asset_android_client.data.translator.UserTranslator
import com.ttvnp.ttj_asset_android_client.domain.model.*
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import com.ttvnp.ttj_asset_android_client.domain.util.Now
import com.ttvnp.ttj_asset_android_client.domain.util.addHour
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File
import java.io.IOException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
        private val userService: UserService,
        private val stellarService: StellarService,
        private val userDataStore: UserDataStore,
        private val otherUserDataStore: OtherUserDataStore,
        private val stellarAccountDataStore: StellarAccountDataStore
) : UserRepository {

    override fun checkValidationStellar(accountId: String, assetType: AssetType): Single<ErrorCode> {
        return Single.create { subscriber ->
            val singleAccountExecute = stellarService.getSingleAccount(accountId).execute()
            if (!singleAccountExecute.isSuccessful) {
                subscriber.onSuccess(ErrorCode.ERROR_VALIDATION_STELLAR_ACCOUNT)
                return@create
            }
            val singleAccount = singleAccountExecute.body()
            singleAccount?.let { it ->
                try {
                    val target = it.balances.first {
                        BuildConfig.ISSUER_ACCOUNT_ID == it.asset_issuer && assetType.rawValue == it.asset_code
                    }
                    val trustLimit = stellarService.removeDecimal(target.limit ?: "0")
                    if (trustLimit == 0.toLong()) {
                        subscriber.onSuccess(ErrorCode.ERROR_VALIDATION_STELLAR_TRUST_LINE)
                        return@create
                    }
                } catch (e: Exception) {
                    subscriber.onSuccess(ErrorCode.ERROR_VALIDATION_STELLAR_TRUST_LINE)
                }
            }
            subscriber.onSuccess(ErrorCode.NO_ERROR)
        }
    }

    override fun getStellarAccount(): Single<StellarAccountModel> {
        return Single.create { subscriber ->
            var stellarAccountModel: StellarAccountModel? = null
            val refresh: Boolean
            var stellarAccountEntity = stellarAccountDataStore.get()
            if (stellarAccountEntity == null) {
                refresh = true
            } else {
                val localCacheExpiry = stellarAccountEntity.updatedAt.addHour(24 * 7)
                refresh = Now().after(localCacheExpiry)
                if (!refresh) {
                    stellarAccountModel = StellarAccountTranslator().translate(stellarAccountEntity)
                }
            }

            if (refresh) {
                try {
                    val stellarAccountResponse = userService.getStellarAccount().execute()
                    if (!stellarAccountResponse.isSuccessful) {
                        subscriber.onError(HttpException(stellarAccountResponse))
                        return@create
                    }
                    stellarAccountResponse.body()?.let {
                        if (it.hasError()) return@let
                        val entity = StellarAccountEntity(
                                strAccountID = it.strAccountID,
                                strDepositMemoText = it.strDepositMemoText
                        )
                        stellarAccountEntity = stellarAccountDataStore.update(entity = entity)
                        stellarAccountModel = StellarAccountTranslator().translate(entity)
                    }
                } catch (e: IOException) {
                    //ignore connection exception
                }
            }
            if (stellarAccountModel == null) {
                // get from local db
                stellarAccountModel = StellarAccountTranslator().translate(stellarAccountDataStore.get())
            }
            subscriber.onSuccess(stellarAccountModel ?: StellarAccountModel())
        }
    }

    override fun getUser(forceRefresh: Boolean): Single<UserModel> {
        return Single.create { subscriber ->
            var userModel: UserModel? = null
            var refresh = forceRefresh
            if (!refresh) {
                val userEntity = userDataStore.get()
                if (userEntity == null) {
                    refresh = true
                } else {
                    val localCacheExpiry = userEntity.updatedAt.addHour(24 * 7)
                    refresh = Now().after(localCacheExpiry)
                    if (!refresh) {
                        userModel = UserTranslator().translate(userEntity)
                    }
                }
            }
            if (refresh) {
                try {
                    val userResponse = userService.getUser().execute()
                    if (!userResponse.isSuccessful) {
                        if (subscriber.isDisposed) return@create
                        subscriber.onError(HttpException(userResponse))
                        return@create
                    }
                    userResponse.body()?.let {
                        if (it.hasError()) {
                            return@let
                        }
                        var userEntity = UserEntity(
                                emailAddress = it.emailAddress,
                                profileImageID = it.profileImageID,
                                profileImageURL = it.profileImageURL,
                                firstName = it.firstName,
                                middleName = it.middleName,
                                lastName = it.lastName,
                                address = it.address,
                                genderType = it.genderType,
                                dateOfBirth = it.dateOfBirth,
                                cellphoneNumberNationalCode = it.cellphoneNumberNationalCode,
                                cellphoneNumber = it.cellphoneNumber,
                                idDocument1ImageURL = it.idDocument1ImageURL,
                                idDocument2ImageURL = it.idDocument2ImageURL,
                                isEmailVerified = it.isEmailVerified,
                                isIdentified = it.isIdentified,
                                identificationStatus = it.identificationStatus,
                                grantEmailNotification = it.grantEmailNotification,
                                requirePasswordOnSend = it.requirePasswordOnSend,
                                updatedAt = Now()
                        )
                        userEntity = userDataStore.update(userEntity)
                        userModel = UserTranslator().translate(userEntity)!!
                    }
                } catch (e: IOException) {
                    // ignore connection exception.
                }
            }
            if (userModel == null) {
                // get from local db
                userModel = UserTranslator().translate(userDataStore.get())
            }
            subscriber.onSuccess(userModel!!)
        }
    }

    override fun updateUser(profileImageFile: File?, firstName: String, middleName: String, lastName: String, address: String, genderType: Int, dob: String, cellphoneNumberNationalCode: String, cellphoneNumber: String): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            val original = userDataStore.get()
            if (original == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val profileImageFileBody: MultipartBody.Part
            profileImageFileBody = if (profileImageFile == null) {
                val requestFile = RequestBody.create(okhttp3.MultipartBody.FORM, byteArrayOf())
                MultipartBody.Part.createFormData("profileImageFile", "profile_image", requestFile)
            } else {
                val requestFile = RequestBody.create(okhttp3.MultipartBody.FORM, profileImageFile)
                MultipartBody.Part.createFormData("profileImageFile", "profile_image", requestFile)
            }
            val firstNameBody = RequestBody.create(null, firstName)
            val middleNameBody = RequestBody.create(null, middleName)
            val lastNameBody = RequestBody.create(null, lastName)
            val addressBody = RequestBody.create(null, address)
            val genderBody = RequestBody.create(null, genderType.toString())
            val dobBody = RequestBody.create(null, dob)
            val cellphoneNumberNationalCodeBody = RequestBody.create(null, cellphoneNumberNationalCode)
            val cellphoneNumberBody = RequestBody.create(null, cellphoneNumber)
            try {
                val updateUserResponse = userService.updateUser(profileImageFileBody, firstNameBody, middleNameBody, lastNameBody, addressBody, genderBody, dobBody, cellphoneNumberNationalCodeBody, cellphoneNumberBody).execute()
                if (!updateUserResponse.isSuccessful) {
                    subscriber.onError(HttpException(updateUserResponse))
                    return@create
                }
                updateUserResponse.body()?.let { response ->
                    if (response.hasError()) {
                        val errorCode: ErrorCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_UPLOAD_PROFILE_IMAGE_FILE_TOO_LARGE.rawValue -> ErrorCode.ERROR_UPLOAD_PROFILE_IMAGE_FILE_TOO_LARGE
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        subscriber.onSuccess(ModelWrapper(null, errorCode))
                        return@create
                    }
                    var ue = UserEntity(
                            emailAddress = original.emailAddress,
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName,
                            address = response.address,
                            genderType = response.genderType,
                            dateOfBirth = response.dateOfBirth,
                            cellphoneNumberNationalCode = response.cellphoneNumberNationalCode,
                            cellphoneNumber = response.cellphoneNumber,
                            idDocument1ImageURL = response.idDocument1ImageURL,
                            idDocument2ImageURL = response.idDocument2ImageURL,
                            isEmailVerified = response.isEmailVerified,
                            isIdentified = response.isIdentified,
                            identificationStatus = response.identificationStatus,
                            grantEmailNotification = response.grantEmailNotification,
                            requirePasswordOnSend = response.requirePasswordOnSend,
                            updatedAt = Now()
                    )
                    ue = userDataStore.update(ue)
                    subscriber.onSuccess(ModelWrapper(UserTranslator().translate(ue), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
            }
        }
    }

    override fun uploadIdDocument(faceImageFile: File?, addressImageFile: File?): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            val original = userDataStore.get()
            if (original == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val faceImageFileBody = if (faceImageFile == null) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOf())
                MultipartBody.Part.createFormData("idDocument1", "idDocument1", requestFile)
            } else {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), faceImageFile)
                MultipartBody.Part.createFormData("idDocument1", "idDocument1", requestFile)
            }
            val addressImageFileBody = if (addressImageFile == null) {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArrayOf())
                MultipartBody.Part.createFormData("idDocument2", "idDocument2", requestFile)
            } else {
                val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), addressImageFile)
                MultipartBody.Part.createFormData("idDocument2", "idDocument2", requestFile)
            }

            try {
                val uploadIdDocumentResponse = userService.uploadIdDocument(faceImageFileBody, addressImageFileBody).execute()
                if (!uploadIdDocumentResponse.isSuccessful) {
                    subscriber.onError(HttpException(uploadIdDocumentResponse))
                    return@create
                }
                uploadIdDocumentResponse.body()?.let { response ->
                    if (response.hasError()) {
                        val errorCode: ErrorCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_UPLOAD_PROFILE_IMAGE_FILE_TOO_LARGE.rawValue -> ErrorCode.ERROR_UPLOAD_PROFILE_IMAGE_FILE_TOO_LARGE
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        subscriber.onSuccess(ModelWrapper(null, errorCode))
                        return@create
                    }
                    var ue = UserEntity(
                            emailAddress = original.emailAddress,
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName,
                            address = response.address,
                            genderType = response.genderType,
                            dateOfBirth = response.dateOfBirth,
                            cellphoneNumberNationalCode = response.cellphoneNumberNationalCode,
                            cellphoneNumber = response.cellphoneNumber,
                            idDocument1ImageURL = response.idDocument1ImageURL,
                            idDocument2ImageURL = response.idDocument2ImageURL,
                            isEmailVerified = response.isEmailVerified,
                            isIdentified = response.isIdentified,
                            identificationStatus = response.identificationStatus,
                            grantEmailNotification = response.grantEmailNotification,
                            requirePasswordOnSend = response.requirePasswordOnSend,
                            updatedAt = Now()
                    )
                    ue = userDataStore.update(ue)
                    subscriber.onSuccess(ModelWrapper(UserTranslator().translate(ue), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
            }
        }
    }

    override fun getTargetUser(emailAddress: String): Single<ModelWrapper<OtherUserModel?>> {
        val getModelFromLocal: () -> OtherUserModel? = {
            val otherUserEntity = otherUserDataStore.getByEmailAddress(emailAddress)
            OtherUserTranslator().translate(otherUserEntity)
        }
        return Single.create { subscriber ->

            // self send check
            val self = userDataStore.get()
            if (self?.emailAddress ?: "" == emailAddress) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_SELF_SEND))
                return@create
            }

            var model: OtherUserModel? = null
            var errCode = ErrorCode.ERROR_UNKNOWN
            try {
                val targetUserResponse = userService.getTargetUser(emailAddress).execute()
                if (!targetUserResponse.isSuccessful) {
                    subscriber.onError(HttpException(targetUserResponse))
                    return@create
                }
                targetUserResponse.body()?.let { response ->
                    if (response.hasError()) {
                        errCode = when (response.errorCode) {
                            ServiceErrorCode.ERROR_DATA_NOT_FOUND.rawValue -> ErrorCode.ERROR_CANNOT_FIND_TARGET_USER
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        return@let
                    }
                    var otherUserEntity = OtherUserEntity(
                            id = response.id,
                            emailAddress = response.emailAddress,
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName
                    )
                    otherUserEntity = otherUserDataStore.update(otherUserEntity)
                    model = OtherUserTranslator().translate(otherUserEntity)!!
                }
            } catch (e: IOException) {
                errCode = ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
            }
            if (model == null) {
                model = getModelFromLocal()
                if (model == null) {
                    subscriber.onSuccess(ModelWrapper(null, errCode))
                } else {
                    subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
                }
            } else {
                subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
            }
        }
    }

    override fun changePassword(oldPassword: String, newPassword: String, retypePassword: String): Single<ModelWrapper<UserModel?>> {
        return Single.create { subscriber ->
            var errCode = ErrorCode.ERROR_UNKNOWN
            var model: UserModel? = null
            try {
                val changePasswordResponse = userService.changePassword(oldPassword, newPassword, retypePassword).execute()
                if (!changePasswordResponse.isSuccessful) {
                    subscriber.onError(HttpException(changePasswordResponse))
                    return@create
                }
                changePasswordResponse.body()?.let { it ->
                    if (it.hasError()) {
                        errCode = when (it.errorCode) {
                            ServiceErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT.rawValue -> ErrorCode.ERROR_OLD_PASSWORD_IS_NOT_CORRECT
                            else -> ErrorCode.ERROR_UNKNOWN_SERVER_ERROR
                        }
                        return@let
                    }
                    val ue = UserEntity()
                    UserTranslator().translate(ue)?.let {
                        model = it
                    }
                }
            } catch (ex: IOException) {
                errCode = ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER
            }
            model?.let {
                subscriber.onSuccess(ModelWrapper(model, ErrorCode.NO_ERROR))
            } ?: subscriber.onSuccess(ModelWrapper(null, errCode))
        }
    }

    override fun updateNotificationSettings(grantEmailNotification: Boolean?): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            var userEntity = userDataStore.get()
            if (userEntity == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val grantEmailNotificationUpdateValue = grantEmailNotification
                    ?: userEntity.grantEmailNotification

            try {
                val updateNotificationSettings = userService.updateNotificationSettings(grantEmailNotificationUpdateValue).execute()
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
                    val ue = UserEntity(
                            emailAddress = userEntity?.emailAddress ?: "",
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName,
                            address = response.address,
                            genderType = response.genderType,
                            dateOfBirth = response.dateOfBirth,
                            cellphoneNumberNationalCode = response.cellphoneNumberNationalCode,
                            cellphoneNumber = response.cellphoneNumber,
                            idDocument1ImageURL = response.idDocument1ImageURL,
                            idDocument2ImageURL = response.idDocument2ImageURL,
                            isEmailVerified = response.isEmailVerified,
                            isIdentified = response.isIdentified,
                            identificationStatus = response.identificationStatus,
                            grantEmailNotification = response.grantEmailNotification,
                            requirePasswordOnSend = response.requirePasswordOnSend,
                            updatedAt = Now()
                    )
                    userEntity = userDataStore.update(ue)
                    subscriber.onSuccess(ModelWrapper(UserTranslator().translate(userEntity), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER, e))
            }
        }
    }

    override fun updateSecuritySettings(requirePasswordOnSend: Boolean?): Single<ModelWrapper<UserModel?>> {
        return Single.create<ModelWrapper<UserModel?>> { subscriber ->
            var userEntity = userDataStore.get()
            if (userEntity == null) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_UNKNOWN))
                return@create
            }
            val requirePasswordOnSendUpdateValue = requirePasswordOnSend
                    ?: userEntity.requirePasswordOnSend

            try {
                val updateSecuritySettings = userService.updateSecuritySettings(requirePasswordOnSendUpdateValue).execute()
                if (!updateSecuritySettings.isSuccessful) {
                    subscriber.onError(HttpException(updateSecuritySettings))
                    return@create
                }
                updateSecuritySettings.body()?.let {
                    val response = it
                    if (response.hasError()) {
                        subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_UPDATE_DEVICE))
                        return@create
                    }
                    val ue = UserEntity(
                            emailAddress = userEntity?.emailAddress ?: "",
                            profileImageID = response.profileImageID,
                            profileImageURL = response.profileImageURL,
                            firstName = response.firstName,
                            middleName = response.middleName,
                            lastName = response.lastName,
                            address = response.address,
                            genderType = response.genderType,
                            dateOfBirth = response.dateOfBirth,
                            cellphoneNumberNationalCode = response.cellphoneNumberNationalCode,
                            cellphoneNumber = response.cellphoneNumber,
                            idDocument1ImageURL = response.idDocument1ImageURL,
                            idDocument2ImageURL = response.idDocument2ImageURL,
                            isEmailVerified = response.isEmailVerified,
                            isIdentified = response.isIdentified,
                            identificationStatus = response.identificationStatus,
                            grantEmailNotification = response.grantEmailNotification,
                            requirePasswordOnSend = response.requirePasswordOnSend,
                            updatedAt = Now()
                    )
                    userEntity = userDataStore.update(ue)
                    subscriber.onSuccess(ModelWrapper(UserTranslator().translate(userEntity), ErrorCode.NO_ERROR))
                }
            } catch (e: IOException) {
                subscriber.onSuccess(ModelWrapper(null, ErrorCode.ERROR_CANNOT_CONNECT_TO_SERVER))
            }
        }
    }
}
