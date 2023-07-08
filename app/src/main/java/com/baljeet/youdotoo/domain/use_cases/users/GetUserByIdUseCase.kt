package com.baljeet.youdotoo.domain.use_cases.users

import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.domain.repository_interfaces.UserRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetUserByIdUseCase @Inject constructor(
    private val repository: UserRepository
){
    suspend operator fun invoke(userId : String): User? {
        return repository.getUserById(userId)
    }
}