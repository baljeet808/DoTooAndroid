package com.baljeet.youdotoo.presentation.ui.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.baljeet.youdotoo.shared.styles.Nunito
import com.baljeet.youdotoo.ui.destinations.ProjectsViewDestination
import com.baljeet.youdotoo.presentation.ui.theme.DotooGreen
import com.baljeet.youdotoo.presentation.ui.theme.DotooOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.flow.collectLatest


@Destination
@Composable
fun SignupView(
    navigator: DestinationsNavigator?
) {
    val context = LocalContext.current

    val viewModel = viewModel<SignupViewModel>()

    LaunchedEffect(key1 = true ){
        viewModel.state.collectLatest { state->
            state.signUpError?.let { error->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
                viewModel.resetState()
            }
            state.userId?.let {
                navigator?.navigate(ProjectsViewDestination)
                viewModel.resetState()
            }
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color =MaterialTheme.colorScheme.background
            ),

        )
    {
        Column(
            modifier = Modifier
                .requiredWidth(600.dp)
                .requiredHeight(320.dp)
                .offset(y = -110.dp)
                .rotate(-15f)
                .background(color = DotooOrange)

        ) {

        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Register",
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = 36.sp,
                modifier = Modifier
                    .padding(top = 70.dp),
                color =MaterialTheme.colorScheme.background,
            )
            Text(
                text = "Start sharing dotoos",
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

                var name by remember {
                    mutableStateOf("")
                }
                var email by remember {
                    mutableStateOf("")
                }
                var password by remember {
                    mutableStateOf("")
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name  = it} ,
                    label = {
                        Text(
                            text = "Your Name",
                            color = MaterialTheme.colorScheme.secondary,

                            )
                    },
                    placeholder = { Text(text = "Your Name") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email  = it} ,
                    label = {
                        Text(
                            text = "Your Email",
                            color = MaterialTheme.colorScheme.secondary,

                            )
                    },
                    placeholder = { Text(text = "Your Email") },
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password  = it} ,
                    label = {
                        Text(
                            text = "Create Password",
                            color =MaterialTheme.colorScheme.secondary
                        )
                    },
                    placeholder = { Text(text = "Create Password") },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )

                Button(
                    onClick = {
                        viewModel.performSignup(name, email, password)
                    },
                    colors = ButtonDefaults
                        .buttonColors(containerColor = DotooGreen),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(10.dp)

                ) {
                    Text(
                        text="Create Account",
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Column(
                modifier = Modifier
                    .padding(bottom = 100.dp, top = 20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

            }

        }

    }


}