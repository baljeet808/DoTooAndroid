package com.baljeet.youdotoo.data.repository_implementations

import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.room.YouDoTooDatabase
import com.baljeet.youdotoo.domain.repository_interfaces.InvitationsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class InvitationsRepositoryImpl @Inject constructor(
    localDB: YouDoTooDatabase
) : InvitationsRepository {

    private val invitationDao = localDB.invitationDao

    override fun getAllInvitations(): Flow<List<InvitationEntity>> {
        return invitationDao.getAllInvitations()
    }

    override fun getAllInvitationsByProjectID(projectId: String): Flow<List<InvitationEntity>> {
        return invitationDao.getAllInvitationsByProjectID(projectId)
    }

    override suspend fun getInvitationById(id: String): InvitationEntity {
        return invitationDao.getInvitationById(id)
    }

    override suspend fun upsertAll(invitations: List<InvitationEntity>) {
        return invitationDao.upsertAll(invitations)
    }

    override suspend fun delete(invitation: InvitationEntity) {
        return invitationDao.delete(invitation)
    }

    override suspend fun deleteAllByProjectId(projectId: String) {
        return invitationDao.deleteAllByProjectId(projectId)
    }
}