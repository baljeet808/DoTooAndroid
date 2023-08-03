package com.baljeet.youdotoo.presentation.ui.login

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.domain.models.getOnBoardPagerContentList
import com.baljeet.youdotoo.presentation.ui.login.components.OnboardingPager
import com.baljeet.youdotoo.presentation.ui.login.components.PolicyLineView
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun SignInView(
    navigateToPolicy: () -> Unit,
    navigateToTermOfUse: () -> Unit,
    attemptToLogin: () -> Unit
) {
    val pagerState = rememberPagerState()
    val list = getOnBoardPagerContentList()


    Box(
        modifier = Modifier
            .background(
                color = if (isSystemInDarkTheme()) {
                    NightDotooDarkBlue
                } else {
                    Color.White
                }
            )
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            /**
             * Fixed App name
             * **/
            Text(
                text = "YouDoToo",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSystemInDarkTheme()) NightDotooLightBlue else LightDotooLightBlue,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 100.dp)
                    .padding(start = 10.dp),
                maxLines = 1,
                textAlign = TextAlign.Center
            )

            /**
             *On boarding screens
             * **/
            HorizontalPager(
                count = list.size,
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(.56F)
            ) {
                OnboardingPager(pagerContent = list[it])
            }


            HorizontalPagerIndicator(
                pagerState = pagerState,
                modifier = Modifier.padding(20.dp),
                activeColor = if(isSystemInDarkTheme()){
                    NightDotooLightBlue
                }else{
                    LightDotooLightBlue
                },
                indicatorWidth = 14.dp
            )


            AnimatedVisibility(
                pagerState.currentPage == 2,

            ) {
                /**
                 * Login and policy button
                 * **/
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(30.dp))
                            .border(
                                width = 1.dp,
                                color = if (isSystemInDarkTheme()) {
                                    NightDotooLightBlue
                                } else {
                                    LightDotooLightBlue
                                },
                                shape = RoundedCornerShape(30.dp)
                            )
                            .clickable(
                                onClick = {
                                    attemptToLogin()
                                }
                            ),
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp,
                            alignment = Alignment.CenterHorizontally
                        ),
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
                            color = if (isSystemInDarkTheme()) NightDotooTextColor else LightDotooTextColor,
                            fontFamily = FontFamily(Nunito.Bold.font),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PolicyLineView(
                        navigateToPolicy = navigateToPolicy,
                        navigateToTermOfUse = navigateToTermOfUse
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SignInPreview() {
    SignInView(
        navigateToPolicy = {},
        navigateToTermOfUse = {},
        attemptToLogin = { }
    )
}

