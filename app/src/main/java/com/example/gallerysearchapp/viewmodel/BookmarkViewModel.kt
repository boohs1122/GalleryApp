package com.example.gallerysearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gallerysearchapp.data.local.PreferenceStorage
import com.example.gallerysearchapp.ui.model.ImageData
import com.example.gallerysearchapp.ui.screen.BookmarkPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val preferenceStorage: PreferenceStorage,
) : ViewModel() {

    private val _bookmarkList = MutableStateFlow(preferenceStorage.getBookmarks())
    val bookmarkIds = _bookmarkList.map { list ->
        list.map { it.thumbnail }.toSet()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private var currentPagingSource: BookmarkPagingSource? = null

    val bookmarkPagingData: Flow<PagingData<ImageData>> = Pager(
        config = PagingConfig(pageSize = 30, enablePlaceholders = false),
        pagingSourceFactory = {
            BookmarkPagingSource(preferenceStorage).also {
                currentPagingSource = it
            }
        }
    ).flow.cachedIn(viewModelScope)

    fun toggleBookmark(item: ImageData) {
        val currentList = preferenceStorage.getBookmarks().toMutableList()
        val isRemoved = currentList.removeAll { it.thumbnail == item.thumbnail }

        if (isRemoved) {
            preferenceStorage.saveBookmarks(currentList)
            _bookmarkList.value = currentList
            currentPagingSource?.invalidate()
        }
    }
}