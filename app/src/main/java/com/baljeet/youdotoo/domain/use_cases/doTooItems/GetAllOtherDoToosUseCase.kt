package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.data.local.entities.DoTooItemEntity
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllOtherDoToosUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    operator fun invoke(tomorrowDateInLong : Long): Flow<List<DoTooItemEntity>> {
        return repository.getAllOtherTasks(tomorrowDateInLong)
    }
}