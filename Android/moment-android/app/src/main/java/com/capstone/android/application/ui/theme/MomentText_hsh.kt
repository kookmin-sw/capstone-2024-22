package com.capstone.android.application.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


@Composable
fun P_SemiBold18(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp)
    )
}


@Composable
fun P_Medium11(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp)
    )
}

@Composable
fun P_Bold30(content: String, color: Color, textAlign: TextAlign){
    Text(
        text = content,
        textAlign = textAlign,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp)
    )
}


@Composable
fun HintText(content : String){
    Text(
        text = content,
        style = TextStyle(
            color = neutral_100,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp)
    )
}

@Preview(apiLevel = 33)
@Composable
fun TextPreview() {
    ApplicationTheme {
        //TextField_onboard()
    }
}