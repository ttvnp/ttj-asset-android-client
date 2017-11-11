package com.ttvnp.ttj_asset_android_client.domain.use_case

import com.ttvnp.ttj_asset_android_client.domain.model.UserModel
import com.ttvnp.ttj_asset_android_client.domain.repository.UserRepository
import io.reactivex.Single
import javax.inject.Inject

interface UserUseCase {

    // get user info
    fun getUser() : Single<UserModel>

}

class UserUseCaseImpl @Inject constructor(
        private val repository: UserRepository
) : UserUseCase {

    override fun getUser(): Single<UserModel> {
        return repository.getUser()
    }

}
