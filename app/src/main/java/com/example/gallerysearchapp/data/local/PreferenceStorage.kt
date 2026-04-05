package com.example.gallerysearchapp.data.local

import android.content.Context
import androidx.core.content.edit
import com.example.gallerysearchapp.ui.model.ImageData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceStorage @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson,
) {
    private val prefs = context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)

    fun getBookmarks(): List<ImageData> {
        val json = prefs.getString(KEY_BOOKMARK, null) ?: return emptyList()
        val type = object : TypeToken<List<ImageData>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveBookmarks(list: List<ImageData>) {
        val json = gson.toJson(list)
        prefs.edit { putString(KEY_BOOKMARK, json) }
    }

    companion object {
        private const val KEY_BOOKMARK = "bookmark_list"
    }
}