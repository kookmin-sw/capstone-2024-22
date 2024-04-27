package com.capstone.android.application.ui.theme


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.android.application.R


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
fun P_Medium(content: String, color: Color, size: TextUnit){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = size)
    )
}
@Composable
fun P_Medium_Oneline(content: String, color: Color, size: TextUnit){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = size),
        overflow = TextOverflow.Clip,
        maxLines = 1
    )
}

@Composable
fun P_Medium8(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 8.sp),
        overflow = TextOverflow.Clip,
        maxLines = 1
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
fun P_Medium14(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp)
    )
}

//옮길 때 전체 수정하기
@Composable
fun P_Medium14_center(content: String, color: Color, Align: TextAlign){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            textAlign = Align)
    )
}


@Composable
fun P_Medium18(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp)
    )
}

@Composable
fun P_ExtraBold16(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp)
    )
}

@Composable
fun P_ExtraBold14(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp)
    )
}
@Composable
fun P_ExtraBold(content: String, color: Color, size: TextUnit){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = size)
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
fun P_Black45(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Black,
            fontSize = 45.sp)
    )
}
@Composable
fun P_Black50(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Black,
            fontSize = 50.sp)
    )
}
@Composable
fun YJ_Bold(content: String, color: Color, size: TextUnit){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = FontMoment.obangFont,
            fontWeight = FontWeight.Bold,
            fontSize = size)
    )
}


@Composable
fun YJ_Bold15(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = FontMoment.obangFont,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp)
    )
}
@Composable
fun YJ_Bold20(content: String, color: Color){
    Text(
        text = content,
        style = TextStyle(
            color = color,
            fontFamily = FontMoment.obangFont,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp)
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

@Composable
fun CountText(timeLeft: Long){

    val min = (timeLeft / 1000) / 60
    val sec = (timeLeft / 1000) % 60

    Text(
        text = String.format("%02d:%02d", min, sec),
        style = TextStyle(
            color = neutral_600,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp)
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReciptTextField(
    hint: String, onValueChanged: (String) -> Unit, text: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text, textcolor: Color, fontweight : FontWeight ,fontsize: TextUnit, type : String
){
    BasicTextField(
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = 1,
        value = text.value,
        onValueChange = onValueChanged,
        singleLine = true,
        textStyle = TextStyle(
            color = textcolor,
            fontFamily = PretendardFamily,
            fontWeight = fontweight,
            fontSize = fontsize,
            textAlign = if(type == "big") TextAlign.Center else TextAlign.Start),
        modifier = Modifier
            .wrapContentSize()
            .background(color = Color.Transparent)
            .padding(horizontal = 0.dp)

    ){
        Row(){
            TextFieldDefaults.TextFieldDecorationBox(
                value = text.value,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                placeholder = {  ReciptHintText(hint, fontweight, fontsize, type) },
                interactionSource = remember { MutableInteractionSource() },
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    start = 0.dp, top = 0.dp, end = 0.dp, bottom = 0.dp
                )
            )
        }
    }
}

@Composable
fun  ReciptHintText(content : String, fontweight : FontWeight ,fontsize: TextUnit, type: String){


    Text(
        modifier = if(type=="big") Modifier.fillMaxWidth() else Modifier.wrapContentSize(),
        text = content,
        style = TextStyle(
            color = neutral_200,
            fontFamily = PretendardFamily,
            fontWeight = fontweight,
            fontSize = fontsize,
            textAlign = TextAlign.Center)
    )
}

@Preview(apiLevel = 33)
@Composable
fun TextPreview() {
    ApplicationTheme {
        //ReciptTextField()
    }
}