package com.baljeet.youdotoo.presentation.ui.invitation

import android.content.res.Configuration
import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.common.AccessTypeEditor
import com.baljeet.youdotoo.common.getSampleInvitation
import com.baljeet.youdotoo.common.getSampleProfile
import com.baljeet.youdotoo.common.getUsersInvitations
import com.baljeet.youdotoo.data.local.entities.InvitationEntity
import com.baljeet.youdotoo.data.local.entities.UserEntity
import com.baljeet.youdotoo.data.mappers.toUserEntity
import com.baljeet.youdotoo.domain.models.UserInvitation
import com.baljeet.youdotoo.presentation.ui.invitation.components.InvitationItemView
import com.baljeet.youdotoo.presentation.ui.invitation.components.InvitePeopleHeading
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.shared.views.bottomSheets.SelectAccessTypeSheet
import com.baljeet.youdotoo.presentation.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvitationsView(
    invitations: List<InvitationEntity>,
    users: List<UserEntity>,
    onClickActionButton : (userInvitation : UserInvitation) -> Unit,
    onUpdateAccess: (userInvitation : UserInvitation, accessType : Int) -> Unit,
    sendInvite : (email : String, accessType : Int) -> Unit,
    onBackPressed: () -> Unit,
    onSearchQueryChanged : (query : String) -> Unit
) {

    var emailAddress by remember {
        mutableStateOf("")
    }

    var showEmailNotValidError by remember {
        mutableStateOf(false)
    }

    val usersInvitations = getUsersInvitations(users, invitations)

    var selectedUserInvitation : UserInvitation? = null

    val darkTheme = isSystemInDarkTheme()

    val searchBarThemeColor = if (darkTheme) {
        LightDotooFooterTextColor
    } else {
        Color.Gray
    }

    val searchTextColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }

    var accessType by remember {
        mutableStateOf(AccessTypeEditor)
    }

    val sheetState = rememberStandardBottomSheetState(
        skipHiddenState = false,
        initialValue = SheetValue.Hidden
    )
    val sheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.hide()
        }
    }
    val openSheet = {
        scope.launch {
            sheetScaffoldState.bottomSheetState.expand()
        }
    }

    var searchQuery by remember {
        mutableStateOf("")
    }


    BottomSheetScaffold(
        sheetContent = {
            SelectAccessTypeSheet(
                access = accessType,
                onAccessChanged = { selectedAccess ->
                    selectedUserInvitation?.let {
                        //TODO: update invitation access type
                    }?: kotlin.run {
                        accessType = selectedAccess
                    }
                    closeSheet()
                }
            )
        },
        scaffoldState = sheetScaffoldState,
        sheetPeekHeight = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (darkTheme) {
                        NightDotooNormalBlue
                    } else {
                        DotooGray
                    }
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /**
             * Header
             * **/
            InvitePeopleHeading()


            /**
             * Search box
             * **/
            AnimatedVisibility(visible = ( users.size > 5)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = searchBarThemeColor,
                        focusedBorderColor = searchBarThemeColor
                    ),
                    maxLines = 1,
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = "Search icon",
                            tint = searchBarThemeColor
                        )
                    },
                    trailingIcon = {
                        AnimatedVisibility(visible = searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Filled.Cancel,
                                    contentDescription = "Clear query icon",
                                    tint = searchTextColor
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = "Search by name...",
                            fontFamily = FontFamily(Nunito.Normal.font),
                            color = searchBarThemeColor,
                            fontSize = 16.sp
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Nunito.Normal.font),
                        color = searchTextColor,
                        fontSize = 16.sp
                    )
                )
            }


            /**
             *Invitations modifier column
             * **/
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
                    .padding(20.dp)
                    .background(
                        color = if (isSystemInDarkTheme()) {
                            NightTransparentWhiteColor
                        } else {
                            LessTransparentWhiteColor
                        },
                        shape = RoundedCornerShape(20.dp)
                    ),
            ) {

                item { Spacer(modifier = Modifier.height(20.dp)) }

                items(usersInvitations) { userInvitation ->
                    InvitationItemView(
                        userInvitation = userInvitation,
                        onEditAccess = {
                            accessType = userInvitation.invitationEntity?.accessType?:1
                            selectedUserInvitation = userInvitation
                            openSheet()
                        },
                        onClickButton = {

                        }
                    )
                }

            }


            /**
             * custom invites by email
             * **/
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {

                androidx.compose.material3.Text(
                    text = "Not in collaborators list? Add them by email below:",
                    fontSize = 15.sp,
                    fontFamily = FontFamily(Nunito.Bold.font),
                    color = if (isSystemInDarkTheme()) {
                        LightDotooFooterTextColor
                    } else {
                        Color.Gray
                    },
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 5.dp, end = 5.dp),
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(5.dp))
                AnimatedVisibility(visible = showEmailNotValidError) {
                    androidx.compose.material3.Text(
                        text = "Not a valid email. 😅",
                        fontSize = 13.sp,
                        fontFamily = FontFamily(Nunito.Bold.font),
                        color = if (isSystemInDarkTheme()) {
                            DotooPink
                        } else {
                            DoTooRed
                        },
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 5.dp, end = 5.dp),
                        textAlign = TextAlign.End
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    /**
                     * Edit box for access type
                     * **/
                    OutlinedTextField(
                        value = if(accessType == 1){"Editor"} else {"Viewer"},
                        onValueChange = { access ->
                        },
                        label = {},
                        textStyle = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            letterSpacing = TextUnit(value = 2f, TextUnitType.Sp),
                            color = if (isSystemInDarkTheme()){
                                Color.White
                            }else{
                                Color.Black
                            }
                        ),
                        modifier = Modifier
                            .width(125.dp)
                            .padding(start = 5.dp, end = 5.dp)
                            .clickable(
                                onClick = {
                                    selectedUserInvitation = null
                                    openSheet()
                                }
                            ),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = searchBarThemeColor,
                            focusedBorderColor = searchBarThemeColor
                        ),
                        trailingIcon = {
                            Icon(
                                Icons.Filled.ArrowDropDown,
                                contentDescription = "Send invite button",
                                tint = if(isSystemInDarkTheme()){
                                    Color.White
                                }else{
                                    Color.Black
                                }
                            )
                        }
                    )

                    /**
                     * Text field
                     * **/
                    OutlinedTextField(
                        value = emailAddress,
                        onValueChange = { email ->
                            if(showEmailNotValidError){
                                showEmailNotValidError = false
                            }
                            emailAddress = email
                        },
                        label = {
                            androidx.compose.material3.Text(
                                text = "Invite by email",
                                fontFamily = FontFamily(Nunito.Normal.font),
                                color = searchBarThemeColor,
                                fontSize = 14.sp,
                            )
                        },
                        textStyle = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            letterSpacing = TextUnit(value = 2f, TextUnitType.Sp),
                            color = if (isSystemInDarkTheme()){
                                Color.White
                            }else{
                                Color.Black
                            }
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 5.dp, end = 5.dp),
                        maxLines = 1,
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = searchBarThemeColor,
                            focusedBorderColor = searchBarThemeColor
                        ),
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    val emailIsValid = isEmailValid(emailAddress)
                                    showEmailNotValidError = emailIsValid.not()
                                    if(emailIsValid){
                                        //TODO: send invitation
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Send,
                                    contentDescription = "Send invite button",
                                    tint = NightDotooBrightBlue
                                )
                            }
                        }
                    )
                }
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
    }



}

fun isEmailValid(email: String): Boolean {
    if (Patterns.EMAIL_ADDRESS.matcher(email).matches().not()) {
        return false
    }
    return true
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewInvitationsView() {
    InvitationsView(
        invitations = listOf(
            getSampleInvitation(),
            getSampleInvitation()
        ),
        users = listOf(
            getSampleProfile().toUserEntity(),
            getSampleProfile().toUserEntity()
        ),
        onBackPressed = {},
        sendInvite = {_,_ ->},
        onClickActionButton = {},
        onUpdateAccess = {_,_->},
        onSearchQueryChanged = {}
    )
}