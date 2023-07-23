package com.baljeet.youdotoo.presentation.ui.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.domain.models.OnBoardPagerContent
import com.baljeet.youdotoo.domain.models.getOnBoardPagerContentList
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun OnboardingPager(
    pagerContent : OnBoardPagerContent
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Text(
            text = pagerContent.title,
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = TextUnit(45F, TextUnitType.Sp),
            color = if (isSystemInDarkTheme()) {
                NightDotooTextColor
            } else {
                LightDotooTextColor
            },
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )


        Image(
            imageVector = ImageVector.vectorResource(id = pagerContent.res),
            contentDescription = "todo illustration",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .background(color = Color.Transparent)
        )

        Text(
            text = pagerContent.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp),
            textAlign = TextAlign.Center,
            color = if (isSystemInDarkTheme()) {
                NightDotooFooterTextColor
            } else {
                LightDotooFooterTextColor
            },
            fontFamily = FontFamily(Nunito.Normal.font),
            fontSize = 20.sp,
            lineHeight = TextUnit(30F, TextUnitType.Sp)
        )

    }

}

@Preview(showBackground = true)
@Composable
fun PreviewOnBoardingPager(){
    OnboardingPager(
        pagerContent = getOnBoardPagerContentList()[1]
    )
}