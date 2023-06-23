package com.baljeet.youdotoo.shared.views.bottomSheets

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooGreen
import com.baljeet.youdotoo.util.DueDates

@Composable
fun DueDatesSheet(
    dueDate: DueDates,
    onDateSelected : (DueDates) -> Unit,
    onDatePickerSelected: () -> Unit
) {
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
                text = "Select Due Date",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.height(1.dp))
            for (date in DueDates.values().toCollection(ArrayList()).filter { d -> d != DueDates.CUSTOM }){
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onDateSelected(date)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text = date.toString,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start
                    )
                    Checkbox(
                        checked = date == dueDate,
                        onCheckedChange = {
                            onDateSelected(date)
                        },
                        colors = CheckboxDefaults.colors(
                            checkmarkColor = MaterialTheme.colorScheme.background,
                            checkedColor = DotooGreen
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.height(1.dp))
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onDatePickerSelected()
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Or Pick Custom Date & Time",
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Icon(
                    Icons.Default.EditCalendar,
                    contentDescription ="Pick From Calendar Button",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.padding(end = 10.dp)
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(modifier = Modifier.height(0.dp))
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}


@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun dueDateSheetPreview(){
    DueDatesSheet(dueDate = DueDates.INDEFINITE, onDatePickerSelected = { }, onDateSelected = {} )
}