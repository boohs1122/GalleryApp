package com.example.gallerysearchapp.data.repository

import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument
import com.example.gallerysearchapp.ui.screen.SearchPagingKey
import com.example.gallerysearchapp.ui.screen.SearchResult
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchImages(query: String): Flow<List<ImageDocument>>
    fun searchVideos(query: String): Flow<List<VideoDocument>>

    suspend fun getSearchImagesAndVideos(query: String, page: Int, size: Int, isImageEnd: Boolean, isVideoEnd: Boolean): SearchResult
}