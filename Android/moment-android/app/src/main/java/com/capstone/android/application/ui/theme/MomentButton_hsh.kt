package com.capstone.android.application.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.capstone.android.application.R


@Composable
fun BigButton(name : String, enable : Boolean, onClick : () -> Unit){
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()
    val bgColor = if(isPressed) tertiary_500 else primary_500
    val contentColor = if(isPressed) black else white

    Button(modifier = Modifier
        .fillMaxWidth(),
        enabled = enable,
        shape =  RoundedCornerShape(3.dp),
        onClick = { onClick()  },
        border = if(isPressed) BorderStroke(1.dp, primary_500)
                else BorderStroke(0.dp, neutral_200),

        interactionSource = interactionSource,
        content = {
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = PretendardFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp)
            )
        },

        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = contentColor,
            disabledContainerColor = neutral_200,
            disabledContentColor = neutral_500),

        contentPadding = PaddingValues(vertical = 16.dp)
        )
}

@Composable
fun BackButton_Onboarding(onClick : () -> Unit){
        Image(painter = painterResource(R.drawable.ic_btn_back_onboarding),
            contentDescription = "로그인에서 뒤로가는 버튼",
            modifier = Modifier
                .width(8.dp)
                .height(14.dp)
                .padding(end = 0.dp)
                .clickable { onClick() })

}

@Composable
fun CheckButton(){

    val isChecked = remember { mutableStateOf(false) }

    Checkbox(
        modifier = Modifier
            .height(4.dp)
            .width(4.dp),
        checked = isChecked.value,
        onCheckedChange = {
            isChecked.value = it
        },
        colors = CheckboxDefaults.colors(
            checkedColor = primary_500,
            uncheckedColor = black
        )
    )
}

@Preview(apiLevel = 33)
@Composable
fun btnPreview() {
    ApplicationTheme {
        CheckButton()
    }
}