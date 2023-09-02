package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.data.local.entities.TaskEntity
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetDoTooByIdUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    suspend operator fun invoke(doTooId : String): TaskEntity {
        return repository.getDoTooById(doTooId)
    }
}