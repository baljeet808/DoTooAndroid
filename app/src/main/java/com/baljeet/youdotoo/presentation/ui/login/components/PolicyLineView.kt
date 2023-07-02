package com.baljeet.youdotoo.presentation.ui.login.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.baljeet.youdotoo.presentation.ui.shared.styles.Nunito
import com.baljeet.youdotoo.presentation.ui.theme.DotooBlue

/**
 * Updated by Baljeet singh.
 * **/
@Composable
fun PolicyLineView(
    navigateToPolicy : () -> Unit,
    navigateToTermOfUse : () -> Unit
) {
    val initialText = "By signing up, you agree to the "
    val termOfService = "Term of Service"
    val and = " and "
    val privacyPolicy = "Privacy Policy"
    val dot = "."

    val normalStyle = SpanStyle(
        color = Color.Gray,
        fontFamily = FontFamily(Nunito.Normal.font),
        fontSize = 12.sp
    )

    val clickableStyle = SpanStyle(
        color = DotooBlue,
        fontFamily = FontFamily(Nunito.Normal.font),
        fontSize = 12.sp
    )

    val annotatedString = buildAnnotatedString {
        withStyle(style = normalStyle){
            append(initialText)
        }
        withStyle(style = clickableStyle){
            pushStringAnnotation(tag = termOfService, annotation = termOfService)
            append(termOfService)
        }
        withStyle(style = normalStyle){
            append(and)
        }
        withStyle(style = clickableStyle){
            pushStringAnnotation(tag = privacyPolicy, annotation = privacyPolicy)
            append(privacyPolicy)
        }
        withStyle(style = normalStyle){
            append(dot)
        }
    }

    ClickableText(
        text = annotatedString,
        style = TextStyle(
            textAlign = TextAlign.Center),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 60.dp),
        onClick= {offset ->
            annotatedString.getStringAnnotations(start = offset, end = offset)
                .firstOrNull()?.also { span ->
                        when(span.item){
                            privacyPolicy ->{
                                navigateToPolicy()
                            }
                            termOfService ->{
                                navigateToTermOfUse()
                            }
                        }
                }
        }
    )
    
}


@Preview(showBackground = true)
@Composable
fun PreviewPolicyLineView(){
    PolicyLineView( navigateToPolicy = { /*TODO*/ }) {
        
    }
}