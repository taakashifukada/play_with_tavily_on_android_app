package com.example.tavilyplayground.repository

import com.example.tavilyplayground.entity.ApiQuery
import com.example.tavilyplayground.entity.ApiResponse
import com.example.tavilyplayground.network.TavilyApi
import javax.inject.Inject

class MainRepository @Inject constructor() {

    suspend fun searchJson(query: ApiQuery): ApiResponse {
        return TavilyApi.retrofitService.searchJson(query = query)
    }
}