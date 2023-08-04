package com.baljeet.youdotoo.presentation.ui.createproject

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.*
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun CreateProjectView(
    createProject: (name : String, description : String, color : Long) -> Unit,
    navigateBack : () -> Unit
) {

    var projectName by remember {
        mutableStateOf("")
    }

    var description by remember {
        mutableStateOf("")
    }

    var selectedColor by remember {
        mutableStateOf(getRandomColorEnum())
    }


    var descriptionOn by remember {
        mutableStateOf(false)
    }

    var showColorOptions by remember {
        mutableStateOf(false)
    }

    val hapticFeedback = LocalHapticFeedback.current

    val transition = rememberInfiniteTransition()

    val rotation = transition.animateValue(
        initialValue = -3f,
        targetValue =  3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 100),
            repeatMode = RepeatMode.Reverse
        ),
        typeConverter = Float.VectorConverter
    )

    var showTitleErrorAnimation by remember{
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = showTitleErrorAnimation){
        delay(1000)
        showTitleErrorAnimation = false
    }

    /**
     * Main content
     * **/
    Column(
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightNormalThemeColor
                } else {
                    DotooGray
                }
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {

        /**
         * Row for top close button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {


            Text(
                text = "Create Project",
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f),
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.secondary
            )


            IconButton(
                onClick = navigateBack,
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(40.dp)
                    )
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Button to close side drawer.",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooTextColor
                    } else {
                        Color.Black
                    }
                )
            }
        }

        /**
         * Row for preview and project color button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            //Preview
            Row(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                    .clickable(
                        onClick = {

                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Preview,
                    contentDescription = "Button to see the project preview.",
                    tint = LightAppBarIconsColor,
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Preview",
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.width(5.dp))

            //Select Color
            Row(
                modifier = Modifier
                    .border(
                        width = 2.dp,
                        color = if (isSystemInDarkTheme()) {
                            NightDotooFooterTextColor
                        } else {
                            LightDotooFooterTextColor
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, start = 20.dp, end = 20.dp, bottom = 10.dp)
                    .clickable(
                        onClick = {
                            showColorOptions = showColorOptions.not()
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Adjust,
                    contentDescription = "Button to set project color.",
                    tint = Color(selectedColor.longValue),
                    modifier = Modifier
                        .width(30.dp)
                        .height(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = selectedColor.name,
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        AnimatedVisibility(visible = showColorOptions) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ){
                item {
                    Spacer(modifier = Modifier.width(20.dp))
                }
                items(EnumProjectColors.values()){ color ->
                    Row(
                        modifier = Modifier
                            .border(
                                width = 2.dp,
                                color = if(selectedColor == color){
                                    if (isSystemInDarkTheme()) {
                                        Color.White
                                    } else {
                                        Color.Black
                                    }
                                }else{
                                    if (isSystemInDarkTheme()) {
                                        NightDotooFooterTextColor
                                    } else {
                                        LightDotooFooterTextColor
                                    }
                                },
                                shape = RoundedCornerShape(30.dp)
                            )
                            .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                            .clickable(
                                onClick = {
                                    selectedColor = color
                                }
                            ),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Adjust,
                            contentDescription = "Button to set project color.",
                            tint = Color(color.longValue),
                            modifier = Modifier
                                .width(25.dp)
                                .height(25.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = color.name,
                            color = if(selectedColor == color){
                                if (isSystemInDarkTheme()) {
                                    Color.White
                                } else {
                                    Color.Black
                                }
                            }else{
                                LightAppBarIconsColor
                            },
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        /**
         * Row for dotoo additional fields
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(
                40.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            //set priority
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = {

                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Outlined.Favorite,
                    contentDescription = "Button to set project favorite.",
                    tint = LightAppBarIconsColor
                )
            }

            //toggle description
            Row(
                modifier = Modifier
                    .clickable(
                        onClick = {
                            descriptionOn = descriptionOn.not()
                            if (descriptionOn.not()) {
                                description = ""
                            }
                        }
                    ),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (descriptionOn) {
                        Icons.Outlined.PlaylistRemove
                    } else {
                        Icons.Outlined.Notes
                    },
                    contentDescription = "Button to set add description to project.",
                    tint = LightAppBarIconsColor
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = if (descriptionOn) {
                        "Clear Description"
                    } else {
                        "Add Description"
                    },
                    color = LightAppBarIconsColor,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }

        Spacer(modifier = Modifier.height(20.dp))
        /**
         * Text field for adding title
         * **/
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalArrangement = Arrangement.spacedBy(
                1.dp,
                alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Title",
                color = LightAppBarIconsColor,
                fontSize = 13.sp,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp)
            )
            TextField(
                value = projectName,
                onValueChange = {
                    if (it.length <= maxTitleCharsAllowedForProject) {
                        projectName = it
                    }
                },
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = "Enter new project name",
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            Color.Black
                        },
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font),
                        modifier = Modifier
                            .rotate(
                                if (showTitleErrorAnimation) {
                                    rotation.value
                                } else {
                                    0f
                                }
                            )
                    )
                },
                textStyle = TextStyle(
                    color = if (isSystemInDarkTheme()) {
                        DotooGray
                    } else {
                        Color.Black
                    },
                    fontSize = 24.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font)
                ),
                maxLines = 3,
                modifier = Modifier.fillMaxWidth(),

                )
            Text(
                text = "${projectName.length}/$maxTitleCharsAllowedForProject",
                color = if (projectName.length >= maxTitleCharsAllowedForProject) {
                    DoTooRed
                } else {
                    LightAppBarIconsColor
                },
                fontSize = 13.sp,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        AnimatedVisibility(visible = descriptionOn) {
            Spacer(modifier = Modifier.height(40.dp))
        }

        /**
         * Text field for adding description
         * **/
        AnimatedVisibility(visible = descriptionOn) {


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                verticalArrangement = Arrangement.spacedBy(
                    1.dp,
                    alignment = Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Description",
                    color = LightAppBarIconsColor,
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp)
                )
                TextField(
                    value = description,
                    onValueChange = {
                        if (it.length <= maxDescriptionCharsAllowed) {
                            description = it
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = "Enter description here",
                            color = if (isSystemInDarkTheme()) {
                                DotooGray
                            } else {
                                Color.Black
                            },
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Nunito.SemiBold.font)
                        )
                    },
                    textStyle = TextStyle(
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            Color.Black
                        },
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Nunito.SemiBold.font)
                    ),
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),

                    )
                Text(
                    text = "${description.length}/$maxDescriptionCharsAllowed",
                    color = if (description.length >= maxDescriptionCharsAllowed) {
                        DoTooRed
                    } else {
                        LightAppBarIconsColor
                    },
                    fontSize = 13.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    modifier = Modifier.padding(start = 15.dp)
                )
            }
        }


        Spacer(modifier = Modifier.weight(1f))

        /**
         * Save button
         * **/
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End
        ) {

            Row(
                modifier = Modifier
                    .shadow(elevation = 5.dp, shape = RoundedCornerShape(30.dp))
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            DotooGray
                        } else {
                            NightDotooBrightBlue
                        },
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(top = 10.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
                    .clickable(
                        onClick = {
                            if (projectName.isNotBlank()) {
                                createProject(
                                    projectName,
                                    description,
                                    selectedColor.longValue
                                )
                            } else {
                                projectName = ""
                                showTitleErrorAnimation = true
                            }
                            addHapticFeedback(hapticFeedback = hapticFeedback)
                        }
                    )
            ) {
                Text(
                    text = "New Project",
                    color = if (isSystemInDarkTheme()) {
                        NightDotooBrightBlue
                    } else {
                        Color.White
                    },
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    Icons.Default.ExpandLess,
                    contentDescription = "Create task button",
                    tint = if (isSystemInDarkTheme()) {
                        NightDotooBrightBlue
                    } else {
                        Color.White
                    }
                )
            }
        }
    }

}


@Preview(showBackground = true)
@Composable
fun PreviewCreateProjectView(){
    CreateProjectView(
        createProject = {_,_,_->},
        navigateBack = {}
    )
}