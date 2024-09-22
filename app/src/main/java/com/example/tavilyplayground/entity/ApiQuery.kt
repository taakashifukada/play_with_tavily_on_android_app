package com.example.tavilyplayground.entity

import com.squareup.moshi.Json

// https://app.tavily.com/
const val API_KEY = "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"

data class ApiQuery(
    @Json(name = "query")
    val query: String,
    @Json(name = "api_key")
    val apiKey: String = API_KEY,
    @Json(name = "exclude_domains")
    val excludeDomains: List<String> = emptyList(),
    @Json(name = "include_answer")
    val includeAnswer: Boolean = true,
    @Json(name = "include_domains")
    val includeDomains: List<String> = emptyList(),
    @Json(name = "include_image_descriptions")
    val includeImageDescriptions: Boolean = true,
    @Json(name = "include_images")
    val includeImages: Boolean = true,
    @Json(name = "include_raw_content")
    val includeRawContent: Boolean = false,
    @Json(name = "max_results")
    val maxResults: Int = 5,
    @Json(name = "search_depth")
    val searchDepth: String = SearchDepth.ADVANCED.value,
    @Json(name = "days")
    val days: Int = 5,
    @Json(name = "topic")
    val topic: String = Topic.GENERAL.value
)

enum class SearchDepth(val value: String) {
    BASIC("basic"),
    ADVANCED("advanced")
}

enum class Topic(val value: String) {
    GENERAL("general"),
    NEWS("news"),
}