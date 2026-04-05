package com.example.gallerysearchapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gallerysearchapp.viewmodel.SearchViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel,
) {
    // Paging 데이터를 collect합니다.
    val pagedItems = searchViewModel.searchResults.collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "검색 결과",
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // 한 줄에 3개씩 표시
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // 페이징 아이템 리스트 출력
            items(pagedItems.itemCount) { index ->
                val item = pagedItems[index]
                if (item != null) {
                    SearchResultItem(item = item.toString()) // 실제 객체에 맞게 수정
                }
            }

            // 하단 로딩 상태 처리 (추가 데이터 로딩 중일 때)
            pagedItems.apply {
                when {
                    loadState.append is LoadState.Loading -> {
                        item { LoadingIndicator() }
                    }

                    loadState.refresh is LoadState.Loading -> {
                        // 첫 로딩 시 화면 전체 중앙 로딩은 외부에서 처리하거나 여기서 item으로 처리
                    }
                }
            }
        }
    }
}

@Composable
fun SearchResultItem(item: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // 정사각형 비율
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = item)
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}