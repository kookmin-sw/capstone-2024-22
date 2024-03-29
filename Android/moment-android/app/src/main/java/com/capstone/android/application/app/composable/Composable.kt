package com.capstone.android.application.app.composable

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.capstone.android.application.ui.theme.HintText
import com.capstone.android.application.ui.theme.PretendardFamily
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_100
import com.capstone.android.application.ui.theme.tertiary_500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetRecord(sheetState: SheetState, onClicked:() -> Unit,onDismiss:() -> Unit){
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {
//        LazyColumn(
//            modifier = Modifier.padding(bottom = WindowInsets.navigationBars.getBottom(LocalDensity.current).dp/2) ,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ){
//            item {
//                Text(
//                    color = Color("#4992FF".toColorInt()),
//                    text = "녹음",
//                    fontSize = 20.sp,
//                    fontWeight = FontWeight.Bold
//                )
//                Spacer(modifier = Modifier.height(4.dp))
//            }
//
//            }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MomentTextField(
    hint: String, onValueChanged: (String) -> Unit, onClicked: (Boolean) -> Unit, text: MutableState<String>,
    keyboardType: KeyboardType = KeyboardType.Text, changecolor:Color,
    focusRequester: FocusRequester,  move: String, focusManager: FocusManager
){
//move
    /*     manyfirstmove 는 여러개 있는 상황에서 next를 사용해야할 경우  request가 필요
           manynextmove 는 여러개 있는 상황에서 next를 사용해야할 경우  request가 안필요
           manyendmove 는 여러개 있는 상황에서 clear를 사용해야할 경우 request가 안필요
           onemove 는 하나 있는 상황이라 clear를 사용하며 request가 필요*/
//textclick
    BasicTextField(
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        maxLines = 1,
        value = if(text.value.length>20) text.value.removeRange(10,text.value.length) else text.value,
        onValueChange = onValueChanged,
        singleLine = true,
        textStyle = TextStyle(
            color = black,
            fontFamily = PretendardFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp),
        keyboardActions = KeyboardActions(onDone = {
            if(move =="manyfirstmove" || move == "manynextmove") focusManager.moveFocus(
                FocusDirection.Next)
            else focusManager.clearFocus()
        }),
        modifier = if(move == "manyfirstmove" || move == "onemove") Modifier
            .fillMaxWidth()
            .background(
                color = tertiary_500
            )
            .padding(horizontal = 8.dp)
            .focusRequester(focusRequester)
            .clickable { Log.d("tjgus", "MomentTextField: tjgus") }
        else Modifier
            .fillMaxWidth()
            .background(
                color = tertiary_500
            )
            .padding(horizontal = 8.dp)
            .clickable { Log.d("tjgus", "MomentTextField: tjgus") }

    ){
        Row(){
            TextFieldDefaults.TextFieldDecorationBox(
                value = /*if(text.value.length>20) text.value.removeRange(10,text.value.length) else */text.value,
                visualTransformation = VisualTransformation.None,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                // same interaction source as the one passed to BasicTextField to read focus state
                // for text field styling
                placeholder = { if (text.value.isNotEmpty()) HintText(content = "")  else HintText(hint) },
                colors =  TextFieldDefaults.textFieldColors(
                    textColor = black
                ),
                interactionSource = remember { MutableInteractionSource() },
                // keep vertical paddings but change the horizontal
                contentPadding = TextFieldDefaults.textFieldWithoutLabelPadding(
                    start = 0.dp, top = 1.dp, end = 1.dp, bottom = 3.dp
                )
            )
        }
    }
}




