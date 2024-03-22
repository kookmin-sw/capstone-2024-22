package com.capstone.android.application.ui.theme
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.capstone.android.application.R

object FontMoment {
    val preStandardFont = FontFamily(
        Font(R.font.pretendard_light,FontWeight.Light),
        Font(R.font.pretendard_extra_light,FontWeight.ExtraLight),
        Font(R.font.pretendard_medium,FontWeight.Medium),
        Font(R.font.pretendard_bold,FontWeight.Bold),
        Font(R.font.pretendard_extra_bold,FontWeight.ExtraBold),
        Font(R.font.pretendard_semi_bold,FontWeight.SemiBold),
        Font(R.font.pretendard_thin, FontWeight.Thin),
        Font(R.font.pretendard_regular),
    )

    val obangFont = FontFamily(
        Font(R.font.yj_obang_bold,FontWeight.Bold)
    )
}