package com.baljeet.youdotoo.presentation.ui.shared.views

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.R
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import kotlinx.coroutines.delay


@Composable
fun NothingHereView() {



    var animateCat1 by remember{
        mutableStateOf(false)
    }


    val offSetX = animateDpAsState(
        if (animateCat1) {
            (0).dp
        } else {
            (-100).dp
        }
    )

    var animateCat2 by remember{
        mutableStateOf(false)
    }
    var showSecondBOx by remember {
        mutableStateOf(false)
    }

    val offSetX2 = animateDpAsState(
        targetValue = if (animateCat2) {
            (0).dp
        } else {
            (100).dp
        }
    )
    LaunchedEffect(key1 = true){
        delay(1000)
        animateCat1 = true
        delay(4000)
        animateCat1 = false
        delay(3000)
        showSecondBOx = true
        delay(300)
        animateCat2 = true
        delay(5000)
        animateCat2 = false
        delay(200)
        showSecondBOx = false
        delay(2000)
        animateCat1 = true
    }


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AnimatedVisibility(visible = animateCat1) {
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if(isSystemInDarkTheme()){
                        R.drawable.peeping_cat_night
                    }else{
                        R.drawable.peeping_cat_day
                    }
                ),
                contentDescription = "Peeping cat on left",
                modifier = Modifier
                    .width(100.dp)
                    .offset(
                        x = offSetX.value,
                        y = 0.dp
                    )
            )
            Spacer(modifier = Modifier.width(40.dp))
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Nothing Here",
                fontFamily = FontFamily(Nunito.ExtraBold.font),
                fontSize = 38.sp,
                color = MaterialTheme.colorScheme.secondary
            )
            Text(
                text = "Let's add new task.           ",
                fontFamily = FontFamily(Nunito.Bold.font),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(
            visible = showSecondBOx
        ) {
            Spacer(modifier = Modifier.width(40.dp))
            Image(
                imageVector = ImageVector.vectorResource(
                    id = if(isSystemInDarkTheme()){
                        R.drawable.peeping_cat_night
                    }else{
                        R.drawable.peeping_cat_day
                    }
                ),
                contentDescription = "Peeping cat on right",
                modifier = Modifier
                    .width(100.dp)
                    .offset(
                        x = offSetX2.value,
                        y = 0.dp
                    )
                    .graphicsLayer {
                        rotationZ = 180f
                        rotationX = 180f
                    }
            )
        }
    }


}

@Preview(showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewNothingHereView(){
    NothingHereView()
}
