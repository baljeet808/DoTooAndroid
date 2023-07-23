package com.baljeet.youdotoo.domain.use_cases.invitation

import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.domain.repository_interfaces.InvitationsRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UpsertAllInvitationsUseCase @Inject constructor(
    private val repository: InvitationsRepository
){
    suspend operator fun invoke(invitations : List<InvitationEntity>)  {
        return repository.upsertAll(invitations)
    }
}