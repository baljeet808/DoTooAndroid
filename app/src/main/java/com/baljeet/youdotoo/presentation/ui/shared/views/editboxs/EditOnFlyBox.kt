package com.baljeet.youdotoo.presentation.ui.shared.views.editboxs

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.getRandomColor
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooRed
import com.baljeet.youdotoo.presentation.ui.theme.DotooGray

@Composable
fun EditOnFlyBox(
    modifier: Modifier,
    onSubmit : (String) -> Unit,
    placeholder : String,
    label : String,
    maxCharLength : Int,
    onCancel : () -> Unit,
    themeColor : Color,
    lines: Int
) {

    var text  by remember {
        mutableStateOf(placeholder)
    }

    Column(
        modifier = modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        /**
         * Text field
         * **/
        OutlinedTextField(
            value = text,
            onValueChange = { updatedText ->
                if (updatedText.length <= maxCharLength) {
                    text = updatedText
                }
            },
            label = {
                Text(
                    text = label,
                    color = Color.Black,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    letterSpacing = TextUnit(value = 2f, TextUnitType.Sp)
                )
            },
            textStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = FontFamily(Nunito.Bold.font),
                letterSpacing = TextUnit(value = 2f, TextUnitType.Sp),
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, end = 5.dp),
            minLines = lines,
            maxLines = lines,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White,
                disabledContainerColor = Color.White,
                errorContainerColor = Color.White,
                cursorColor = Color.Black,
                errorCursorColor = Color.Red
            )
        )

        /**
         * Top row for counter text, save and cancel button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(15.dp))
            IconButton(
                onClick = {
                    onSubmit(text)
                },
                modifier = Modifier
                    .background(
                        color = DotooGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .height(30.dp)
                    .width(30.dp)
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = "Submit button.",
                    tint = themeColor
                )
            }

            Spacer(modifier = Modifier.width(15.dp))

            IconButton(
                onClick = onCancel,
                modifier = Modifier
                    .background(
                        color = DotooGray,
                        shape = RoundedCornerShape(5.dp)
                    )
                    .height(30.dp)
                    .width(30.dp)

            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Cancel Button",
                    tint = themeColor
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${text.length}/$maxCharLength",
                color = if (text.length >= maxCharLength) {
                    DoTooRed
                } else {
                    Color.White
                },
                fontSize = 13.sp,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.width(15.dp))
        }
    }

}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditOnFlyBox(){
    EditOnFlyBox(
        modifier = Modifier,
        onSubmit = {},
        onCancel = {},
        placeholder = "This is the placeholder text, for testing purpose. Which should acquire two lines.",
        label = "This is label",
        maxCharLength = 40,
        themeColor = Color(getRandomColor()),
        lines = 2
    )
}