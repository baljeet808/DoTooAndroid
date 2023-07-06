package com.baljeet.youdotoo.presentation.ui.drawer.components

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baljeet.youdotoo.presentation.ui.theme.*

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun TopBar(
    notificationsState : Boolean,
    onMenuItemClick: () -> Unit,
    onNotificationItemClicked: () -> Unit,
    onSearchItemClicked: (query: String) -> Unit,
    modifier: Modifier
) {

    var searchQuery by remember {
        mutableStateOf("")
    }

    var toggleSearch by remember {
        mutableStateOf(false)
    }


    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            /**
             * Menu icon to open side nav drawer
             * **/
            IconButton(
                onClick = {
                    onMenuItemClick()
                },
                modifier = Modifier
                    .width(35.dp)
                    .height(35.dp)
                    .weight(0.2f)
            ) {
                Icon(
                    Icons.Outlined.Menu,
                    contentDescription = "Menu button to open side drawer.",
                    tint = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    },
                    modifier = Modifier.width(35.dp).height(35.dp)
                )
            }

            Column(
                modifier = Modifier
                    .height(50.dp)
                    .width(180.dp)
            ) {
                AnimatedVisibility(
                    visible = toggleSearch
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                            .clip(shape = RoundedCornerShape(20.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = if (isSystemInDarkTheme()) {
                                NightDotooDarkBlue
                            } else {
                                LightAppBarIconsColor
                            },
                            focusedIndicatorColor = Color.Transparent,
                            errorIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                onSearchItemClicked(searchQuery)
                            }
                        )
                    )
                }

            }


            /**
             * Menu icon to open side nav drawer
             * **/
            IconButton(
                onClick = {
                    toggleSearch = toggleSearch.not()
                },
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Icon(
                    if (toggleSearch) {
                        Icons.Filled.SearchOff
                    } else {
                        Icons.Outlined.Search
                    },
                    contentDescription = "Button to search projects",
                    tint = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    }
                )
            }
            /**
             * Menu icon to open side nav drawer
             * **/
            IconButton(
                onClick = {
                    onNotificationItemClicked()
                },
                modifier = Modifier
                    .weight(0.2f)
            ) {
                Icon(
                    Icons.Outlined.Message,
                    contentDescription = "Menu button to open Chat",
                    tint = if (isSystemInDarkTheme()) {
                        NightAppBarIconsColor
                    } else {
                        LightAppBarIconsColor
                    }
                )
                AnimatedVisibility(visible = notificationsState) {
                    Box(
                        modifier = Modifier
                            .height(6.dp)
                            .width(6.dp)
                            .background(
                                color = NightDotooBrightPink,
                                shape = RoundedCornerShape(5.dp)
                            )
                    )
                    Spacer(modifier = Modifier
                        .height(15.dp)
                        .width(14.dp))
                }

            }

        }

    }


}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopAppBar() {
    TopBar(
       modifier = Modifier,
        notificationsState = true,
        onMenuItemClick = {},
        onNotificationItemClicked = {},
        onSearchItemClicked = {}
    )
}
