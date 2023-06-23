package com.baljeet.youdotoo.presentation.ui.shared.views

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.baljeet.youdotoo.domain.models.User
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito

@Composable
fun profilesLazyRow(
    profiles: List<User>,
    onTapProfiles : ()->Unit
) {

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        contentPadding = PaddingValues(all = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp, alignment = Alignment.End)
    ) {

        val profilesCount = profiles.count()
        items(
            profiles.take(
                if (profilesCount > 3) {
                    2
                } else 3
            )
        ) { profile ->
            AsyncImage(
                model = profile.avatarUrl,
                contentDescription = "avatarImage",
                modifier = Modifier
                    .shadow(
                        elevation = 2.dp,
                        shape = RoundedCornerShape(10.dp),
                        clip = true
                    )
                    .width(24.dp)
                    .height(24.dp)
                    .clip(shape = RoundedCornerShape(20.dp))
            )
        }

        if (profilesCount > 3) {
            item {
                Button(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(20.dp),
                            clip = true
                        )
                        .width(24.dp)
                        .height(24.dp)
                        .clip(shape = RoundedCornerShape(20.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = (profiles.size - 2).toString().plus("+"),
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.Normal.font),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }

}