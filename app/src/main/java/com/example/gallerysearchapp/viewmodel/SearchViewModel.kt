package com.example.gallerysearchapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gallerysearchapp.data.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    fun fetchData(query: String) {
        viewModelScope.launch {
            searchRepository.searchImages(query)
                .catch { e -> Log.e("SearchTest", "이미지 에러: ${e.message}") }
                .collect { images ->
                    Log.d("SearchTest", "이미지 검색 결과: ${images.size}개 가져옴")
                    images.take(3).forEach { img ->
                        Log.d("SearchTest", "이미지 URL: ${img.imageUrl}")
                    }
                }

            searchRepository.searchVideos(query)
                .catch { e -> Log.e("SearchTest", "동영상 에러: ${e.message}") }
                .collect { videos ->
                    Log.d("SearchTest", "동영상 검색 결과: ${videos.size}개 가져옴")
                    videos.take(3).forEach { video ->
                        Log.d("SearchTest", "동영상 제목: ${video.title}")
                    }
                }
        }
    }


}