package com.example.gallerysearchapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.gallerysearchapp.ui.components.GalleryNavHost
import com.example.gallerysearchapp.ui.components.GalleryTopBar
import com.example.gallerysearchapp.ui.theme.GallerySearchAppTheme
import com.example.gallerysearchapp.viewmodel.BookmarkViewModel
import com.example.gallerysearchapp.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val bookmarkViewModel: BookmarkViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            GallerySearchAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    topBar = {
                        GalleryTopBar(
                            navController = navController,
                            currentRoute = currentRoute,
                            onSearch = { query ->
                                searchViewModel.fetchData(query)
                            }
                        )
                    }
                ) { innerPadding ->
                    GalleryNavHost(
                        navController = navController,
                        searchViewModel = searchViewModel,
                        bookmarkViewModel = bookmarkViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    companion object{
        const val PAGE_LOAD_LIMIT = 30
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    GallerySearchAppTheme {
//        Greeting("Android")
//    }
}