package com.example.gallerysearchapp.data.repository

import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument
import kotlinx.coroutines.flow.Flow

interface SearchRepository {

    fun searchImages(query: String): Flow<List<ImageDocument>>
    fun searchVideos(query: String): Flow<List<VideoDocument>>

}