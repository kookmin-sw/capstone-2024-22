package com.capstone.android.application.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.capstone.android.application.R


val PretendardFamily = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black),
    Font(R.font.pretendard_bold, FontWeight.Bold),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight),
    Font(R.font.pretendard_light, FontWeight.Light),
    Font(R.font.pretendard_medium, FontWeight.Medium),
    Font(R.font.pretendard_regular, FontWeight.Normal),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold),
    Font(R.font.pretendard_thin, FontWeight.Thin)
)


// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = PretendardFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    )
)