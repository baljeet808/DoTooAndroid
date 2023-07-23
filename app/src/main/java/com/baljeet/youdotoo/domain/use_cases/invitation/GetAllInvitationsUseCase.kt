package com.baljeet.youdotoo.domain.use_cases.invitation

import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.domain.repository_interfaces.InvitationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class GetAllInvitationsUseCase @Inject constructor(
    private val repository: InvitationsRepository
){
    operator fun invoke(): Flow<List<InvitationEntity>> {
        return repository.getAllInvitations()
    }
}