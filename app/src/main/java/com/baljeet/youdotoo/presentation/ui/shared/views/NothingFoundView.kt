package com.baljeet.youdotoo.presentation.ui.shared.views

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getRandomNothingFoundIllustration
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray
import com.baljeet.youdotoo.presentation.ui.theme.LightAppBarIconsColor
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooDarkBlue


@Composable
fun NothingFoundView(
    modifier: Modifier,
    headingText : String = "Nothing found here",
    helperText : String = "Let's add some new tasks.",
    nightColor : Color = DotooGray,
    dayColor : Color = LightAppBarIconsColor,
    hideHeading : Boolean = false,
    hideHelperText : Boolean = false,
    hideIllustration : Boolean = false,
    onClick : () -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = if (isSystemInDarkTheme()){
                    NightDotooDarkBlue
                }else{
                    DotooGray
                },
                shape = RoundedCornerShape(20.dp)
            )
            .padding(20.dp)
            .clickable(
                onClick = onClick
            )
        ,
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(hideHeading.not()) {
            Text(
                text = headingText,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) {
                    nightColor
                } else {
                    dayColor
                }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if(hideIllustration.not()) {
            Image(
                imageVector = ImageVector.vectorResource(id = getRandomNothingFoundIllustration()),
                contentDescription = "todo illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 130.dp),
                colorFilter = ColorFilter.tint(
                    color = if (isSystemInDarkTheme()) {
                        nightColor
                    } else {
                        dayColor
                    }
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        if(hideHelperText.not()) {
            Text(
                text = helperText,
                fontFamily = FontFamily(Nunito.Normal.font),
                fontSize = 18.sp,
                color = if (isSystemInDarkTheme()) {
                    nightColor
                } else {
                    dayColor
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewNothingFoundView(){
    NothingFoundView(
        modifier = Modifier.fillMaxWidth(),
        onClick  = {}
    )
}
