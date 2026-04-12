package com.example.gallerysearchapp.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.gallerysearchapp.viewmodel.BookmarkViewModel
import com.example.gallerysearchapp.viewmodel.SearchUiEvent

@Composable
fun BookmarkScreen(
    bookmarkViewModel: BookmarkViewModel,
    event: (SearchUiEvent) -> Unit
) {
    val bookmarkItems = bookmarkViewModel.bookmarkPagingData.collectAsLazyPagingItems()
    val bookmarkIds = bookmarkViewModel.bookmarkIds.collectAsStateWithLifecycle().value

    Column(modifier = Modifier.fillMaxSize()) {
        if (bookmarkItems.itemCount == 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "보관된 항목이 없습니다.")
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(bookmarkItems.itemCount) { index ->
                    val item = bookmarkItems[index]
                    if (item != null) {
                        SearchResultItem(
                            item = item,
                            isBookmarked = bookmarkIds.contains(item.thumbnail),
                            event = event
                        )
                    }
                }
            }
        }
    }
}