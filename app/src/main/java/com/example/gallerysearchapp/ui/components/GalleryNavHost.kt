package com.example.gallerysearchapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.gallerysearchapp.ui.screen.BookmarkScreen
import com.example.gallerysearchapp.ui.screen.SearchScreen
import com.example.gallerysearchapp.viewmodel.SearchViewModel

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Search : Screen("search", "검색", Icons.Default.Search)
    object Bookmark : Screen("bookmark", "보관함", Icons.Default.Favorite)
}

@Composable
fun GalleryNavHost(
    navController: NavHostController,
    searchViewModel: SearchViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
        composable(Screen.Search.route) {
            SearchScreen(searchViewModel)
        }
        composable(Screen.Bookmark.route) {
            BookmarkScreen()
        }
    }
}