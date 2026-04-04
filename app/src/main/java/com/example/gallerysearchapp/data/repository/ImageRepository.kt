package com.example.gallerysearchapp.data.repository

interface ImageRepository {
    fun getPhotos(): List<String>
}