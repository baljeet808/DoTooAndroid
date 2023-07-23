package com.baljeet.youdotoo.presentation.ui.invitation

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.baljeet.youdotoo.common.getSampleInvitation
import com.baljeet.youdotoo.data.local.entities.InvitationEntity

@Composable
fun InvitationsView (
    invitations : List<InvitationEntity>,
    onBackPressed : () -> Unit
) {

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewInvitationsView(){
    InvitationsView(
        invitations = listOf(
            getSampleInvitation(),
            getSampleInvitation()
        ),
        onBackPressed = {}
    )
}