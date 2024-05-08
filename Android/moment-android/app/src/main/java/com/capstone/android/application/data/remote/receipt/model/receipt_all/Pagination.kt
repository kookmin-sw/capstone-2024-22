package com.capstone.android.application.data.remote.receipt.model.receipt_all

data class Pagination(
    val currentElements: Int,
    val currentPage: Int,
    val totalElements: Int,
    val totalPages: Int
)