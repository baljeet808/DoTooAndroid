package com.baljeet.youdotoo.domain.use_cases.users

import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.domain.repository_interfaces.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetUsersByIdsUseCase @Inject constructor(
    private val repository: UserRepository
){
    operator fun invoke(ids : List<String>): Flow<List<UserEntity>> {
        return repository.getAllUsersOfTheseIds(ids)
    }
}