package com.baljeet.youdotoo.presentation.ui.dotoo.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.common.getSampleDotooItem
import com.baljeet.youdotoo.domain.models.DoTooItem


@Composable
fun DoTooItemsLazyColumn(
    lazyListState: LazyListState,
    doToos : List<DoTooItem>,
    onToggleDoToo : (doToo: DoTooItem) -> Unit,
    onNavigateClick :(doToo : DoTooItem) -> Unit,
    modifier: Modifier
){

    LazyColumn(
        state = lazyListState,
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Transparent),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        items(doToos){ dotoo ->
            DoTooItemView(
                doToo = dotoo,
                onNavigateClick = {
                    onNavigateClick(dotoo)
                },
                onToggleDone = {
                    onToggleDoToo(dotoo)
                }
            )
            if(doToos.last() == dotoo){
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewDoTooItemsLazyColumn(){
    val sampleTaskItem = getSampleDotooItem()
    DoTooItemsLazyColumn(
        lazyListState = LazyListState(),
        doToos = listOf(
            getSampleDotooItem(),
            getSampleDotooItem(),
            getSampleDotooItem(),
            sampleTaskItem
        ),
        onNavigateClick = {},
        onToggleDoToo = {},
        modifier = Modifier
    )
}