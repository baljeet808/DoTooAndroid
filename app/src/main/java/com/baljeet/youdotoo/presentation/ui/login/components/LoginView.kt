package com.baljeet.youdotoo.presentation.ui.login.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.presentation.ui.login.SignInState
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooPink


@Composable
fun LoginView(
     navigateToProjects : ()->Unit,
     navigateToSignUp : ()->Unit,
     attemptToLogin : (email : String, password : String)-> Unit,
     state : SignInState
    ){
    val context = LocalContext.current

    if(state.userId != null){
        navigateToProjects()
    }

    if(state.signInError != null){
        Toast.makeText(context,"Wrong credentials", Toast.LENGTH_SHORT).show()
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            color = MaterialTheme.colorScheme.background
        ),

    )
    {
        Column(
            modifier = Modifier
                .requiredWidth(600.dp)
                .requiredHeight(320.dp)
                .offset(y = (-100).dp)
                .rotate(15f)
                .background(color = DotooPink)

        ) {

        }


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "You Do Too",
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = 36.sp,
                modifier = Modifier
                    .padding(top = 70.dp),
                color = MaterialTheme.colorScheme.background,
            )
            Text(
                text = "Get things done",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(top = 12.dp) ,
                color = MaterialTheme.colorScheme.background
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(start = 40.dp, end = 40.dp, top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                var email by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email  = it} ,
                    label = {
                        Text(
                            text = "Your Email",
                            color = MaterialTheme.colorScheme.secondary,

                        )
                            },
                    placeholder = { Text(text = "Your Email")},
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    )
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password  = it} ,
                    label = {
                        Text(
                            text = "Password",
                            color =MaterialTheme.colorScheme.secondary
                        )},
                    placeholder = { Text(text = "Password")},
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            attemptToLogin(email, password)
                        }
                    )
                )

                Button(
                    onClick = {
                        attemptToLogin(email, password)
                    },
                    colors = ButtonDefaults
                        .buttonColors(containerColor = DotooBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp)

                ) {
                    Text(
                        text="Log In",
                        color = MaterialTheme.colorScheme.background
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 100.dp, top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "New around here?",
                    fontFamily = FontFamily(Nunito.SemiBold.font),
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(top = 12.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                
                TextButton(onClick = {
                    navigateToSignUp()
                }) {
                    Text(
                        text = "Create An Account",
                        fontSize = 15.sp
                    )
                }
            }

        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    YouDoTooTheme {
//        LoginView(navigator = null)
//    }
//}

