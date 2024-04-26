package com.capstone.android.application.app.composable

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.toColorInt
import com.capstone.android.application.ui.theme.ApplicationTheme
import com.capstone.android.application.ui.theme.HintText
import com.capstone.android.application.ui.theme.P_ExtraBold16
import com.capstone.android.application.ui.theme.P_Medium14
import com.capstone.android.application.ui.theme.P_Medium14_center
import com.capstone.android.application.ui.theme.PretendardFamily
import com.capstone.android.application.ui.theme.YJ_Bold15
import com.capstone.android.application.ui.theme.black
import com.capstone.android.application.ui.theme.neutral_100
import com.capstone.android.application.ui.theme.neutral_500
import com.capstone.android.application.ui.theme.neutral_600
import com.capstone.android.application.ui.theme.tertiary_500
import kotlin.math.roundToInt

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

@Composable
fun FancyProgressBar(
    modifier: Modifier,
    progress: Float = 0f.coerceIn(0f, 1f),
    leftColor: Color = Color.Black,
    rightColor: Color = Color("#938F8F".toColorInt()),
    indicatorColor: Color = Color("#99342E".toColorInt()),
    textStyle: TextStyle = TextStyle(color = Color.White),
    cornerRaduis: Dp = 10.dp,
    onDragEnd: (Float) -> Unit,
    onDrag: (Float) -> Unit,
) {

    var offsetX = remember { mutableFloatStateOf(0f) }
    var progressBarWidthInDp = remember { mutableStateOf(Dp(0f)) }

    val guidelinePercentage = remember {
        derivedStateOf {
            Dp(offsetX.value) / progressBarWidthInDp.value
        }
    }

    LaunchedEffect(progress) {
        offsetX.value = progress.coerceIn(0f, 1f).times(progressBarWidthInDp.value).value
    }
    val isAnimatePercentageUp = remember {
        derivedStateOf {
            guidelinePercentage.value < 0.2f || guidelinePercentage.value > 0.8f
        }
    }

    val animation = animateDpAsState(
        targetValue = if (isAnimatePercentageUp.value) -Dp(35f) else Dp(0f),
        label = "Text Animation"
    )
    Box(
        modifier = modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .onSizeChanged {
                progressBarWidthInDp.value = it.width.dp
            },
        contentAlignment = Alignment.Center
    ) {

        val constraints = ConstraintSet {
            //References of widgets in the layout
            val leftBox = createRefFor("leftBox")
            val rightBox = createRefFor("rightBox")
            val indicator = createRefFor("indicator")

            val leftPercentage = createRefFor("leftPercentage")
            val rightPercentage = createRefFor("rightPercentage")

            val guideLine = createGuidelineFromStart(guidelinePercentage.value)

            //set constraints to the widgets
            constrain(leftBox)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(guideLine, margin = 0.dp)
                width = Dimension.fillToConstraints
                height = Dimension.preferredValue(4.dp)
            }


            constrain(leftPercentage)
            {
                top.linkTo(leftBox.top)
                bottom.linkTo(leftBox.bottom)
                start.linkTo(leftBox.start, margin = 5.dp)
            }

            constrain(rightPercentage)
            {
                top.linkTo(leftBox.top)
                bottom.linkTo(leftBox.bottom)
                end.linkTo(rightBox.end, margin = 5.dp)
            }

            constrain(rightBox)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                start.linkTo(guideLine, margin = 0.dp)
                width = Dimension.fillToConstraints
                height = Dimension.preferredValue(4.dp)
            }

            constrain(indicator)
            {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                height = Dimension.preferredValue(12.dp)
            }

        }
        ConstraintLayout(
            constraints,
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
        ) {

            //Left progress
            Box(
                modifier = Modifier
                    .layoutId("leftBox")
                    .background(
                        shape = RoundedCornerShape(cornerRaduis),
                        color = leftColor
                    )
            )

            //Right progress
            Box(
                modifier = Modifier
                    .layoutId("rightBox")
                    .background(
                        shape = RoundedCornerShape(cornerRaduis),
                        color = rightColor
                    )
            )

            Box(modifier = Modifier
                .layoutId("indicator")
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .width(5.dp)
                .background(shape = RoundedCornerShape(5.dp), color = indicatorColor)
                .pointerInput(Unit) {
                    detectDragGestures(onDragEnd = { onDragEnd(guidelinePercentage.value) }) { change, dragAmount ->
                        change.consume()
                        offsetX.value = (offsetX.value + dragAmount.x)
                            .coerceIn(0f, progressBarWidthInDp.value.value)
                        onDrag(guidelinePercentage.value)
                    }

                }
            )

            Text(
                text = "${String.format("%.0f", guidelinePercentage.value * 100)}%",
                modifier = Modifier
                    .layoutId("leftPercentage")
                    .offset(y = animation.value),
                style = textStyle
            )

            Text(
                text = "${String.format("%.0f", (1 - guidelinePercentage.value) * 100)}%",
                modifier = Modifier
                    .layoutId("rightPercentage")
                    .offset(y = animation.value),
                style = textStyle
            )

        }
    }
}



@Composable
fun CustomNoTitleCheckDialog(
    description: String,
    checkleft: String,
    checkright: String,
    onClickCancel: () -> Unit,
    onClickleft: () -> Unit,
    onClickright: () -> Unit
){
    Dialog(onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(horizontal = 40.dp)){
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(tertiary_500)) {

                Column(
                    Modifier
                        .wrapContentSize()
                        .padding(top = 20.dp, bottom = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    P_Medium14_center(content = description, color = neutral_600, TextAlign.Center)
                }

                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Column(
                        Modifier
                            .padding(8.dp)
                            .weight(0.2f)
                            .clickable { onClickleft() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        YJ_Bold15(content = checkleft, color = black)
                    }
                    Divider(
                        Modifier
                            .width(2.dp)
                            .height(20.dp), neutral_500)

                    Column(
                        Modifier
                            .padding(8.dp)
                            .weight(0.2f)
                            .clickable { onClickright() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        YJ_Bold15(content = checkright, color = black)
                    }
                }
            }
        }
    }
}




// 제목 다이얼로그
@Composable
fun CustomTitleCheckDialog(
    title : String,
    description: String,
    checkleft: String,
    checkright: String,
    onClickCancel: () -> Unit,
    onClickleft: () -> Unit,
    onClickright: () -> Unit
){
    Dialog(onDismissRequest = { onClickCancel() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(horizontal = 40.dp)){
            Column(modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(tertiary_500)) {

                Column(
                    Modifier
                        .wrapContentSize()
                        .padding(top = 20.dp, bottom = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    P_ExtraBold16(content = title, color = black)
                    Spacer(modifier = Modifier.height(16.dp))
                    P_Medium14_center(content = description, color = neutral_600, TextAlign.Center)
                }

                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Column(
                        Modifier
                            .padding(8.dp)
                            .weight(0.2f)
                            .clickable { onClickleft() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        YJ_Bold15(content = checkleft, color = black)
                    }
                    Divider(
                        Modifier
                            .width(2.dp)
                            .height(20.dp), neutral_500)

                    Column(
                        Modifier
                            .padding(8.dp)
                            .weight(0.2f)
                            .clickable { onClickright() },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        YJ_Bold15(content = checkright, color = black)
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun ReciptPreview() {
    ApplicationTheme {
    }
}

