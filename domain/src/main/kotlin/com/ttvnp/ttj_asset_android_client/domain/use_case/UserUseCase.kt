package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.model.BalancesModel
import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.BalanceRepository
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

interface UserUseCase {

    fun getUser(): Single<UserModel>

    fun getBalances(): Single<BalancesModel>
}

class UserUseCaseImpl @Inject constructor(
        private val userRepository: UserRepository,
        private val balanceRepository: BalanceRepository
) : UserUseCase {

    override fun getUser(): Single<UserModel> {
        return userRepository.getUser()
    }

    override fun getBalances(): Single<BalancesModel> {
        return balanceRepository.getBalances()
    }
}
