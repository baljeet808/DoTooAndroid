package com.baljeet.youdotoo.domain.use_cases.invitation

import com.baljeet.youdotoo.domain.repository_interfaces.InvitationsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DeleteAllInvitationsByProjectIdUseCase @Inject constructor(
    private val repository: InvitationsRepository
){
    suspend operator fun invoke(projectId : String)  {
        return repository.deleteAllByProjectId(projectId)
    }
}