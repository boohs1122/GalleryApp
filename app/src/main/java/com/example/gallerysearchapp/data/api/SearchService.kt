package com.example.gallerysearchapp.data.api

import com.example.gallerysearchapp.data.model.ImageResponse
import com.example.gallerysearchapp.data.model.VideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET("v2/search/image")
    suspend fun searchImage(
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 30,
    ): ImageResponse

    @GET("v2/search/vclip")
    suspend fun searchVideo(
        @Query("query") query: String,
        @Query("sort") sort: String = "accuracy",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 15,
    ): VideoResponse
}