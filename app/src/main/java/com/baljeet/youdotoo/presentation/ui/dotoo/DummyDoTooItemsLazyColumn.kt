package com.baljeet.youdotoo.presentation.ui.dotoo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.domain.models.DoTooItem


@Composable
fun DummyDoTooItemsLazyColumn(
    doToos: List<DoTooItem>,
    modifier: Modifier,
    textColor : Color = Color.White,
    backgroundColor : Color = Color(getRandomColor())
) {


    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(doToos, key = {it.id}) { dotoo ->
            DummyDoTooItemView(
                doToo = dotoo,
                textColor = textColor,
                backgroundColor = backgroundColor
            )
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDummyDoTooItemsLazyColumn() {
    val sampleTaskItem = getSampleDotooItem()
    DummyDoTooItemsLazyColumn(
        doToos = listOf(
            getSampleDotooItem(),
            sampleTaskItem
        ),
        modifier = Modifier
    )
}