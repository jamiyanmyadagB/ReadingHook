package com.readingnook.memoryplus.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.readingnook.memoryplus.model.Page

/**
 * Type converters for Room database.
 * 
 * Handles conversion of complex types to/from database columns.
 */
class Converters {
    
    private val gson = Gson()
    
    @TypeConverter
    fun fromPageList(pages: List<Page>): String {
        return gson.toJson(pages)
    }
    
    @TypeConverter
    fun toPageList(pagesString: String): List<Page> {
        val listType = object : TypeToken<List<Page>>() {}.type
        return gson.fromJson(pagesString, listType) ?: emptyList()
    }
}
