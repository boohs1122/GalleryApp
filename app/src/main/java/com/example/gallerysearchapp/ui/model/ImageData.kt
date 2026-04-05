package com.example.gallerysearchapp.ui.model

import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument

data class ImageData(
    val thumbnail: String,
    val url: String,
    val dateTime: String,
    val type: SearchType,
    val title: String
) {
    companion object {
        fun from(imageDto: ImageDocument): ImageData {
            return ImageData(
                thumbnail = imageDto.thumbnailUrl,
                url = imageDto.imageUrl,
                dateTime = imageDto.dateTime,
                type = SearchType.IMAGE,
                title = imageDto.siteName
            )
        }

        fun from(videoDto: VideoDocument): ImageData {
            return ImageData(
                thumbnail = videoDto.thumbnailUrl,
                url = videoDto.url,
                dateTime = videoDto.dateTime,
                type = SearchType.VIDEO,
                title = videoDto.title
            )
        }
    }

}

enum class SearchType {
    IMAGE, VIDEO
}

