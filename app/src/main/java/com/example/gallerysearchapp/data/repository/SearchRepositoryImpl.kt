package com.example.gallerysearchapp.data.repository

import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument
import com.example.gallerysearchapp.data.api.SearchService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
}