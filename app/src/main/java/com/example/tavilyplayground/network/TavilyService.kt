package com.example.tavilyplayground.network

import com.example.tavilyplayground.entity.ApiQuery
import com.example.tavilyplayground.entity.ApiResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.tavily.com/"

interface TavilyService {
    @FormUrlEncoded
    @Headers("Content-Type: application/json")
    @POST("search")
    suspend fun search(
        @Field("api_key") apiKey: String,
        @Field("query") query: String,
        @Field("exclude_domains") excludeDomains: List<String>,
        @Field("include_answer") includeAnswer: Boolean,
        @Field("include_domains") includeDomains: List<String>,
        @Field("include_image_descriptions") includeImageDescriptions: Boolean,
        @Field("include_images") includeImages: Boolean,
        @Field("include_raw_content") includeRawContent: Boolean,
        @Field("max_results") maxResults: Int,
        @Field("search_depth") searchDepth: String,
        @Field("days") days: Int,
        @Field("topic") topic: String
    ): ApiResponse

    @Headers("Content-Type: application/json")
    @POST("search")
    suspend fun searchJson(
        @Body query: ApiQuery
    ): ApiResponse
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(40, TimeUnit.SECONDS)
    .readTimeout(40, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(client)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addConverterFactory(ScalarsConverterFactory.create())
    .build()

object TavilyApi {
    val retrofitService: TavilyService by lazy {
        retrofit.create(TavilyService::class.java)
    }
}