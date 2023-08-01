package com.baljeet.youdotoo.domain.repository_interfaces

import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import kotlinx.coroutines.flow.Flow

interface InvitationsRepository {

     fun getAllInvitations(): Flow<List<InvitationEntity>>

     fun getAllInvitationsByProjectID(projectId : String): Flow<List<InvitationEntity>>

     suspend fun getInvitationById(id : String): InvitationEntity?

     suspend fun upsertAll(invitations : List<InvitationEntity>)

     suspend fun delete(invitation : InvitationEntity)

     suspend fun deleteAllByProjectId(projectId: String)

}