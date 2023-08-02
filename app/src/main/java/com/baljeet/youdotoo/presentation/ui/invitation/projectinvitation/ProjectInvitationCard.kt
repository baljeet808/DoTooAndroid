package com.baljeet.youdotoo.presentation.ui.invitation.projectinvitation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getSampleInvitation
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.LessTransparentWhiteColor
import com.baljeet.youdotoo.presentation.ui.theme.NightTransparentWhiteColor


@Composable
fun ProjectInvitationCard(
    invitation: InvitationEntity?,
    modifier: Modifier
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color(invitation?.projectColor ?: 4278215265))
            .padding(10.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {


        Canvas(modifier = Modifier.fillMaxWidth(), onDraw = {
            drawCircle(
                color = NightTransparentWhiteColor,
                radius = 230.dp.toPx(),
                center = Offset(
                    x = 40.dp.toPx(),
                    y = 100.dp.toPx()
                )
            )
            drawCircle(
                color = Color(invitation?.projectColor ?: 4278215265),
                radius = 100.dp.toPx(),
                center = Offset(
                    x = 50.dp.toPx(),
                    y = 100.dp.toPx()
                )
            )

            //creating lines using canvas
            for (i in 1..6) {
                drawLine(
                    color = NightTransparentWhiteColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(
                        x = (170 + (i * 25)).dp.toPx(),
                        y = (0).dp.toPx()
                    ),
                    end = Offset(
                        x = (160).dp.toPx(),
                        y = (10 + (i * 25)).dp.toPx()
                    )
                )
            }
            for (i in 1..8) {
                drawLine(
                    color = NightTransparentWhiteColor,
                    strokeWidth = 4.dp.toPx(),
                    start = Offset(
                        x = (320 + (i * 25)).dp.toPx(),
                        y = (0).dp.toPx()
                    ),
                    end = Offset(
                        x = (135 + (i * 25)).dp.toPx(),
                        y = (185).dp.toPx()
                    )
                )
            }
        })

        Text(
            text = invitation?.projectName?:"No Title",
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            fontFamily = FontFamily(Nunito.ExtraBold.font),
            fontSize = 38.sp,
            color = Color.White,
            lineHeight = TextUnit(49f, TextUnitType.Sp)
        )

        AnimatedVisibility(visible = invitation?.projectDetail != null) {
            Text(
                text = invitation?.projectDetail!!,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp),
                color = LessTransparentWhiteColor,
                fontSize = 16.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))


    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewProjectInvitationCard() {
    ProjectInvitationCard(
        invitation = getSampleInvitation(),
        modifier = Modifier
    )
}