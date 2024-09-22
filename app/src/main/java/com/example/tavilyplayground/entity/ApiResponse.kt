package com.example.tavilyplayground.entity

data class ApiResponse(
    val answer: String?,
    val follow_up_questions: String?,
    val images: List<Image>,
    val query: String,
    val response_time: Float,
    val results: List<Result>
)