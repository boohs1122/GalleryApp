package com.example.gallerysearchapp.data.model

import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("is_end") val isEnd: Boolean,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("total_count") val totalCount: Int,
)

data class ImageResponse(
    val meta: Meta,
    val documents: List<ImageDocument>,
)

data class ImageDocument(
    @SerializedName("thumbnail_url") val thumbnailUrl: String,
    @SerializedName("image_url") val imageUrl: String,
    @SerializedName("display_sitename") val siteName: String,
    @SerializedName("datetime") val dateTime: String,
)

data class VideoResponse(
    val meta: Meta,
    val documents: List<VideoDocument>,
)

data class VideoDocument(
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail") val thumbnailUrl: String,
    @SerializedName("play_time") val playTime: Int,
    @SerializedName("author") val author: String,
    @SerializedName("datetime") val dateTime: String,
)