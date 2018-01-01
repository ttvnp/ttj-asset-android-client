package com.ttvnp.ttj_asset_android_client.presentation.di.module

import com.ttvnp.ttj_asset_android_client.data.repository.DeviceRepositoryImpl
import com.ttvnp.ttj_asset_android_client.data.repository.UserRepositoryImpl
import com.ttvnp.ttj_asset_android_client.data.repository.BalanceRepositoryImpl
import com.ttvnp.ttj_asset_android_client.data.repository.UserTransactionRepositoryImpl
import com.ttvnp.ttj_asset_android_client.data.service.DeviceService
import com.ttvnp.ttj_asset_android_client.data.service.DeviceServiceWithNoAuth
import com.ttvnp.ttj_asset_android_client.data.service.RecaptchaService
import com.ttvnp.ttj_asset_android_client.data.service.UserService
import com.ttvnp.ttj_asset_android_client.data.store.*
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.DeviceRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserTransactionRepository
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.DeviceUseCaseImpl
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCase
import com.ttvnp.ttj_asset_android_client.domain.use_case.UserUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    // use cases
    @Provides
    fun deviceUseCase(deviceRepository: DeviceRepository): DeviceUseCase {
        return DeviceUseCaseImpl(deviceRepository)
    }

    @Provides
    fun userUseCase(
            userRepository: UserRepository,
            balanceRepository: BalanceRepository,
            userTransactionRepository: UserTransactionRepository
    ): UserUseCase {
        return UserUseCaseImpl(userRepository, balanceRepository, userTransactionRepository)
    }

    // repository
    @Provides
    fun deviceRepository(
            deviceServiceWithNoAuth: DeviceServiceWithNoAuth,
            deviceService: DeviceService,
            deviceDataStore: DeviceDataStore,
            deviceInfoDataStore: DeviceInfoDataStore,
            userDataStore: UserDataStore,
            recaptchaService: RecaptchaService
    ): DeviceRepository {
        return DeviceRepositoryImpl(
                deviceServiceWithNoAuth,
                deviceService,
                deviceDataStore,
                deviceInfoDataStore,
                userDataStore,
                recaptchaService
        )
    }

    @Provides
    fun userRepository(
            userService: UserService,
            userDataStore: UserDataStore,
            otherUserDataStore: OtherUserDataStore
    ): UserRepository {
        return UserRepositoryImpl(userService, userDataStore, otherUserDataStore)
    }

    @Provides
    fun balanceRepository(
            userService: UserService,
            balanceDataStore: BalanceDataStore
    ): BalanceRepository {
        return BalanceRepositoryImpl(userService, balanceDataStore)
    }

    @Provides
    fun userTransactionRepository(
            userService: UserService,
            userTransactionDataStore: UserTransactionDataStore,
            appDataStore: AppDataStore,
            deviceInfoDataStore : DeviceInfoDataStore
    ): UserTransactionRepository {
        return UserTransactionRepositoryImpl(userService, userTransactionDataStore, appDataStore, deviceInfoDataStore)
    }
}