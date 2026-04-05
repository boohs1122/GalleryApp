package com.example.gallerysearchapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gallerysearchapp.ui.model.ImageData
import com.example.gallerysearchapp.ui.model.SearchType
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
                    SearchResultItem(item = item)
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
fun SearchResultItem(item: ImageData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f), // 1:1 정사각형 비율 (3열 그리드에 최적)
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // 이미지 로딩
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.thumbnail)
                    .crossfade(true) // 부드러운 전환 효과
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop, // 영역에 꽉 차게 자르기
                modifier = Modifier.fillMaxSize()
            )

            // 비디오 타입인 경우 우측 상단이나 중앙에 아이콘 표시
            if (item.type == SearchType.VIDEO) {
                Icon(
                    imageVector = Icons.Default.PlayCircle,
                    contentDescription = item.title,
                    tint = Color.White.copy(alpha = 0.9f), // 살짝 투명한 흰색
                    modifier = Modifier
                        .align(Alignment.TopEnd) // 우측 상단 배치
                        .padding(8.dp)           // 여백
                        .size(28.dp)             // 크기 조절
                        .background(
                            color = Color.Black.copy(alpha = 0.3f), // 아이콘 뒤에만 살짝 어두운 배경
                            shape = CircleShape
                        )
                        .padding(2.dp)
                )
            }
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