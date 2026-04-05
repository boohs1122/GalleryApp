package com.example.gallerysearchapp.ui.screen

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.gallerysearchapp.data.local.PreferenceStorage
import com.example.gallerysearchapp.ui.model.ImageData

class BookmarkPagingSource(
    private val preferenceStorage: PreferenceStorage,
) : PagingSource<Int, ImageData>() {

    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.let {
                it.prevKey?.plus(1) ?: it.nextKey?.minus(1)
            }
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        return try {
            val page = params.key ?: 0
            val allBookmarks = preferenceStorage.getBookmarks()

            val fromIndex = page * params.loadSize
            val toIndex = minOf(fromIndex + params.loadSize, allBookmarks.size)

            if (fromIndex >= allBookmarks.size) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null,
                )
            }

            LoadResult.Page(
                data = allBookmarks.subList(fromIndex, toIndex),
                prevKey = if (page == 0) null else page - 1,
                nextKey = if (toIndex < allBookmarks.size) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}