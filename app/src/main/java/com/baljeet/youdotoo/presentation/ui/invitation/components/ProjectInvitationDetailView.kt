package com.baljeet.youdotoo.presentation.ui.invitation.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.baljeet.youdotoo.common.getSampleInvitation
import com.baljeet.youdotoo.common.getSampleProject
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.ProjectEntity
import com.baljeet.youdotoo.data.mappers.toProjectEntity

@Composable
fun ProjectInvitationDetailView(
    invitation : InvitationEntity?,
    project : ProjectEntity?,
    acceptInvitation : (InvitationEntity) -> Unit,
    declineInvitation : (InvitationEntity) -> Unit
) {


}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectInvitationDetailView(){
    ProjectInvitationDetailView(
        invitation = getSampleInvitation(),
        project = getSampleProject().toProjectEntity(),
        acceptInvitation = {},
        declineInvitation = {}
    )
}