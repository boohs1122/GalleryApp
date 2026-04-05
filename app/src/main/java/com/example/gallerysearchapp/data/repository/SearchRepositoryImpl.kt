package com.example.gallerysearchapp.data.repository

import com.example.gallerysearchapp.data.api.SearchService
import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument
import com.example.gallerysearchapp.ui.model.ImageData
import com.example.gallerysearchapp.ui.screen.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: SearchService,
) : SearchRepository {

    override fun searchImages(query: String): Flow<List<ImageDocument>> {
        return flow {
            try {
                val response = apiService.searchImage(query = query)
                emit(response.documents)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override fun searchVideos(query: String): Flow<List<VideoDocument>> {
        return flow {
            try {
                val response = apiService.searchVideo(query = query)
                emit(response.documents)
            } catch (e: Exception) {
                emit(emptyList())
            }
        }
    }

    override suspend fun getSearchImagesAndVideos(
        query: String,
        page: Int,
        size: Int,
        isImageEnd: Boolean,
        isVideoEnd: Boolean,
    ): SearchResult = withContext(Dispatchers.IO) {

        val imageRequestSize = if (isVideoEnd) size else size / 2
        val videoRequestSize = if (isImageEnd) size else size / 2

        val imageDeferred = async {
            if (isImageEnd) null
            else runCatching {
                apiService.searchImage(query = query, page = page, size = imageRequestSize)
            }.getOrNull()
        }

        val videoDeferred = async {
            if (isVideoEnd) null
            else runCatching {
                apiService.searchVideo(query = query, page = page, size = videoRequestSize)
            }.getOrNull()
        }

        val imageRes = imageDeferred.await()
        val videoRes = videoDeferred.await()

        val result = (imageRes?.documents.orEmpty() + videoRes?.documents.orEmpty()).map { document ->
            when (document) {
                is ImageDocument -> ImageData.from(imageDto = document)
                is VideoDocument -> ImageData.from(videoDto = document)
                else -> throw IllegalStateException("Unknown document type")
            }
        }.sortedByDescending { it.dateTime }

        return@withContext SearchResult(
            data = result,
            isImageEnd = isImageEnd || (imageRes?.meta?.isEnd ?: true),
            isVideoEnd = isVideoEnd || (videoRes?.meta?.isEnd ?: true)
        )
    }

    companion object {
        const val SIZE_LIMIT = 15
    }
}