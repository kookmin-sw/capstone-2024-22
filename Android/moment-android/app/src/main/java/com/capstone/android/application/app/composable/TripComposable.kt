package com.capstone.android.application.app.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.capstone.android.application.ui.theme.neutral_600

@Composable
fun TripExist(tripName:String,remainPeriod:Int){
    Column {
        Text(
            text = tripName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row {
            Text(
                text = "출발까지",
                color = Color("#706969".toColorInt()),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${remainPeriod}",
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                color = Color("#99342E".toColorInt())
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = "일 남았아요",
                color = Color("#706969".toColorInt()),
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun TripIng(tripName:String,remainPeriod:Int){
    Column {
        Text(
            text = tripName,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Row {
            Text(
                text = "여행시작",
                color = Color("#706969".toColorInt()),
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "${remainPeriod}",
                fontWeight = FontWeight.Bold,
                fontSize = 45.sp,
                color = Color("#99342E".toColorInt())
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                modifier = Modifier.align(Alignment.Bottom),
                text = "일 차",
                color = Color("#706969".toColorInt()),
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun TripEmpty(text:String){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = neutral_600
        )

    }
}