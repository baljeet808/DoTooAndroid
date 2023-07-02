package com.baljeet.youdotoo.presentation.ui.login.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.common.ConstFirstScreenDescription
import com.baljeet.youdotoo.presentation.ui.login.SignInState
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DoTooLightBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue
import com.baljeet.youdotoo.presentation.ui.theme.DotooDarkerGray


@Composable
fun SignInView(
    navigateToProjects: () -> Unit,
    navigateToSignUp: () -> Unit,
    navigateToPolicy : () -> Unit,
    navigateToTermOfUse : () -> Unit,
    attemptToLogin: (email: String, password: String) -> Unit,
    state: SignInState
) {
    val context = LocalContext.current


    if (state.signInError != null) {
        Toast.makeText(context, "Wrong credentials", Toast.LENGTH_SHORT).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    )
    {

        Text(
            text = "You do it too.",
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = DotooBlue,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 100.dp),
            maxLines = 1
        )
        Text(
            text = "YouDoToo",
            fontFamily = FontFamily(Nunito.Bold.font),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = if(isSystemInDarkTheme())Color.Gray else DoTooLightBlue,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 100.dp)
                .padding(start = 10.dp)
            ,
            maxLines = 1,

            )

        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.todo_illustration),
            contentDescription = "todo illustration",
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 300.dp)
                .background(color = Color.Transparent)
        )

        Text(
            text = ConstFirstScreenDescription,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 30.dp, end = 30.dp),
            textAlign = TextAlign.Center,
            color = Color.Gray,
            fontFamily = FontFamily(Nunito.Normal.font)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(
                    width = 1.dp,
                    color = DoTooLightBlue,
                    shape = RoundedCornerShape(30.dp)
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.google_icon),
                contentDescription = "google icon",
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
                    .background(color = Color.Transparent)
            )
            Text(
                text = "Continue With Google",
                color =  if(isSystemInDarkTheme()) DoTooLightBlue else DotooBlue,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(30.dp))
                .border(
                    width = 1.dp,
                    color = DoTooLightBlue,
                    shape = RoundedCornerShape(30.dp)
                ),
            horizontalArrangement = Arrangement.spacedBy(10.dp, alignment = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painterResource(id = R.drawable.apple_icon),
                contentDescription = "Apple icon",
                modifier = Modifier
                    .width(30.dp)
                    .height(30.dp)
                    .background(color = Color.Transparent)
            )
            Text(
                text = "Continue With Apple",
                color = if(isSystemInDarkTheme()) DoTooLightBlue else DotooBlue,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = DoTooLightBlue)
            )
            Text(
                text = "Or",
                modifier = Modifier
                    .padding(start = 5.dp, end = 5.dp),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Nunito.SemiBold.font),
                fontSize = 14.sp
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .background(color = DoTooLightBlue)
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(shape = RoundedCornerShape(30.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = DotooBlue
            )
        ) {
            Text(
                text = "Sign Up",
                color = DoTooLightBlue,
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

        PolicyLineView(
            navigateToPolicy = navigateToPolicy,
            navigateToTermOfUse = navigateToTermOfUse
        )

    }
}

@Preview(showBackground = true)
@Composable
fun SignInPreview() {
    SignInView(
        navigateToProjects = {},
        navigateToSignUp = {},
        navigateToPolicy = {},
        navigateToTermOfUse = {},
        attemptToLogin = { _, _ -> },
        state = SignInState()
    )
}

