package com.example.gallerysearchapp.ui.model

import com.example.gallerysearchapp.data.model.ImageDocument
import com.example.gallerysearchapp.data.model.VideoDocument

data class ImageData(
    val thumbnail: String,
    val url: String,
    val dateTime: String,
    val type: SearchType,
) {
    companion object {
        fun from(imageDto: ImageDocument): ImageData {
            return ImageData(
                thumbnail = imageDto.thumbnailUrl,
                url = imageDto.imageUrl,
                dateTime = imageDto.dateTime,
                type = SearchType.IMAGE
            )
        }

        fun from(videoDto: VideoDocument): ImageData {
            return ImageData(
                thumbnail = videoDto.thumbnailUrl,
                url = videoDto.url,
                dateTime = videoDto.dateTime,
                type = SearchType.VIDEO
            )
        }
    }

}

enum class SearchType {
    IMAGE, VIDEO
}

