package com.baljeet.youdotoo.permissions.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.getDarkThemeColor


@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onClickOk: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier
) {

    val darkTheme = isSystemInDarkTheme()

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        text = {
            Text(
                text = permissionTextProvider.getDescription(isPermanentlyDeclined),
                fontFamily = FontFamily(Nunito.Normal.font),
                color = Color.Gray
            )
        },
        buttons = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Divider()
                Text(
                    text = if (isPermanentlyDeclined) {
                        "Grant permission"
                    } else "OK",
                    color = if(darkTheme){
                        Color.White
                    }else{
                        Color.Black
                    },
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isPermanentlyDeclined) {
                                onGoToAppSettingsClick()
                            } else {
                                onClickOk()
                            }
                        }
                        .padding(16.dp)
                )
            }
        },
        title = {
            Text(
                text = "Permission required",
                fontFamily = FontFamily(Nunito.Bold.font),
                color = if(darkTheme){
                    Color.White
                }else{
                    Color.Black
                }
            )
        },
        backgroundColor = getDarkThemeColor()
    )

}

interface PermissionTextProvider{
    fun getDescription(isPermanentlyDeclined: Boolean) : String
}

class NotificationPermissionTextProvider : PermissionTextProvider{
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return  if(isPermanentlyDeclined){
            "It seems you permanently declined notification permissions. " +
                    "You can go to app settings to grant it again."
        }else{
            "This app needs access to post notifications " +
                    "In order to keep you updated with tasks deadlines."
        }
    }

}