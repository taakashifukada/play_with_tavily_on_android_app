package com.example.tavilyplayground.entity

data class Result(
    val content: String,
    val raw_content: String?,
    val score: Double,
    val title: String,
    val url: String
)