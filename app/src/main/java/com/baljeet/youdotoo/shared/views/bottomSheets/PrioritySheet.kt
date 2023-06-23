package com.baljeet.youdotoo.shared.views.bottomSheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooGreen
import com.baljeet.youdotoo.util.Priorities


@Composable
fun PrioritySheet(
    priority: Priorities,
    onPriorityChanged : (Priorities) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {

        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            Text(
                text = "Select Priority",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.height(1.dp))
            for (priorityEntry in Priorities.values()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onPriorityChanged(priorityEntry)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = priorityEntry.toString,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        fontSize = 18.sp,
                        modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.Start
                    )
                    Checkbox(
                        checked = priorityEntry == priority,
                        onCheckedChange = {
                            onPriorityChanged(priorityEntry)
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colorScheme.background,
                            checkedColor = DotooGreen
                        ),
                        modifier = Modifier.weight(0.1f)
                    )
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }

}

@Preview(showBackground = true)
@Composable
fun PrioritySheetPreview(){
    PrioritySheet(priority = Priorities.LOW, onPriorityChanged = {})
}