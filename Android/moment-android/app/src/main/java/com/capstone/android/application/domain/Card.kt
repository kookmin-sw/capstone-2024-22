package com.capstone.android.application.domain

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.capstone.android.application.domain.response.card.CardView

data class Card(
    var isDelete: MutableState<Boolean> = mutableStateOf(false),
    var isFavorite: MutableState<Boolean> = mutableStateOf(false),
    var isExpand: MutableState<Boolean> = mutableStateOf(false),
    var cardView: CardView
)