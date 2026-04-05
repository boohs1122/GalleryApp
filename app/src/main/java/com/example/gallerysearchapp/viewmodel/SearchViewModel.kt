package com.example.gallerysearchapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.gallerysearchapp.MainActivity.Companion.PAGE_LOAD_LIMIT
import com.example.gallerysearchapp.data.local.PreferenceStorage
import com.example.gallerysearchapp.data.repository.SearchRepository
import com.example.gallerysearchapp.ui.model.ImageData
import com.example.gallerysearchapp.ui.screen.ImagePagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val preferenceStorage: PreferenceStorage,
) : ViewModel() {

    private val _currentQuery = MutableStateFlow("")

    private val _bookmarkList = MutableStateFlow(preferenceStorage.getBookmarks())
    val bookmarkIds: StateFlow<Set<String>> = _bookmarkList.map { list ->
        list.map { it.thumbnail }.toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptySet()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResults: Flow<PagingData<ImageData>> = _currentQuery
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_LOAD_LIMIT,
                    enablePlaceholders = false,
                    initialLoadSize = PAGE_LOAD_LIMIT
                ),
                pagingSourceFactory = { ImagePagingSource(searchRepository, query) }
            ).flow.cachedIn(viewModelScope)
        }

    fun fetchData(query: String) {
        _currentQuery.value = query
    }

    fun toggleBookmark(item: ImageData) {
        val currentList = _bookmarkList.value.toMutableList()
        val isBookmarked = currentList.any { it.thumbnail == item.thumbnail }

        if (isBookmarked) {
            currentList.removeAll { it.thumbnail == item.thumbnail }
        } else {
            currentList.add(item)
        }
        preferenceStorage.saveBookmarks(currentList)
        _bookmarkList.value = currentList
    }
}