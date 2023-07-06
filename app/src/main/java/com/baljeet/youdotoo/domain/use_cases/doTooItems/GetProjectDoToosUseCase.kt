package com.baljeet.youdotoo.domain.use_cases.doTooItems

import com.baljeet.youdotoo.domain.models.DoTooItem
import com.baljeet.youdotoo.domain.repository_interfaces.DoTooItemsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetProjectDoToosUseCase @Inject constructor(
    private val repository: DoTooItemsRepository
){
    suspend operator fun invoke(projectId : String): List<DoTooItem>  {
        return repository.getDoToos(projectId)
    }
}