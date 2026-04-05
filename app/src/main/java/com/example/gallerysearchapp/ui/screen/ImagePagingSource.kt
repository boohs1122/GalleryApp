package com.example.gallerysearchapp.ui.screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gallerysearchapp.data.repository.SearchRepository
import com.example.gallerysearchapp.ui.model.ImageData

class ImagePagingSource(
    private val searchRepository: SearchRepository,
    private val query: String,
) : PagingSource<SearchPagingKey, ImageData>() {

    override fun getRefreshKey(state: PagingState<SearchPagingKey, ImageData>): SearchPagingKey? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.let {
                it.copy(page = it.page + 1)
            }
        }
    }

    override suspend fun load(params: LoadParams<SearchPagingKey>): LoadResult<SearchPagingKey, ImageData> {
        return try {
            val currentKey = params.key ?: SearchPagingKey(page = 1)
            val result = searchRepository.getSearchImagesAndVideos(
                query = query,
                page = currentKey.page,
                size = params.loadSize,
                isImageEnd = currentKey.isImageEnd,
                isVideoEnd = currentKey.isVideoEnd
            )

            LoadResult.Page(
                data = result.data,
                prevKey = if (currentKey.page == 1) null else currentKey.copy(page = currentKey.page - 1),
                nextKey = if (result.isImageEnd && result.isVideoEnd) {
                    null
                } else if (result.data.isEmpty()) {
                    null
                } else {
                    SearchPagingKey(
                        page = currentKey.page + 1,
                        isImageEnd = result.isImageEnd,
                        isVideoEnd = result.isVideoEnd,
                    )
                }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

data class SearchPagingKey(
    val page: Int,
    val isImageEnd: Boolean = false,
    val isVideoEnd: Boolean = false,
)

data class SearchResult(
    val data: List<ImageData>,
    val isImageEnd: Boolean = false,
    val isVideoEnd: Boolean = false,
)