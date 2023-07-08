package com.baljeet.youdotoo.presentation.ui.shared.views.lazies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooYellow
import com.baljeet.youdotoo.presentation.ui.theme.NightDotooNormalBlue

@Composable
fun ProfilesLazyRow(
    profiles: List<User>,
    onTapProfiles: () -> Unit,
    visiblePictureCount: Int = 3,
    imagesWidthAndHeight : Int = 24,
    spaceBetween : Int = 5,
    lightColor : Color = DoTooYellow
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onTapProfiles),
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(spaceBetween.dp, alignment = Alignment.CenterHorizontally)
    ) {

        val profilesCount = profiles.count()
        items(
            profiles.take(
                if (profilesCount > visiblePictureCount) {
                    visiblePictureCount - 1
                } else visiblePictureCount
            )
        ) { profile ->
            AsyncImage(
                model = profile.avatarUrl,
                contentDescription = "avatarImage",
                modifier = Modifier
                    .width(imagesWidthAndHeight.dp)
                    .height(imagesWidthAndHeight.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
            )
        }

        if (profilesCount > visiblePictureCount) {
            item {
                Column(
                    modifier = Modifier
                        .width(imagesWidthAndHeight.dp)
                        .height(imagesWidthAndHeight.dp)
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(
                            color = if (isSystemInDarkTheme()) {
                                NightDotooNormalBlue
                            } else {
                                Color.White
                            }
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = (profiles.size - (visiblePictureCount - 1)).toString().plus("+"),
                        color = if (isSystemInDarkTheme()) {
                            Color.White
                        } else {
                            lightColor
                        },
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Nunito.Normal.font),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewProfilesLazyRow() {
    ProfilesLazyRow(
        profiles = arrayListOf(
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile(),
            getSampleProfile()
        ),
        onTapProfiles = {},
        visiblePictureCount = 5
    )
}