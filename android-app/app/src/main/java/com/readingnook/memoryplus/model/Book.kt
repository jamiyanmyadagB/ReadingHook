package com.readingnook.memoryplus.model

import org.json.JSONObject

/**
 * Data model representing a book in ReadingNook application.
 * 
 * Contains metadata and page content for translated books.
 * Designed for efficient serialization and Room database compatibility.
 */
data class Book(
    val id: String = "",
    val title: String = "",
    val originalLanguage: String = "",
    val translatedLanguage: String = "",
    val difficulty: String = "",
    val pages: List<Page> = emptyList(),
    val createdAt: String = "",
    val pageCount: Int = 0,
    val isDownloaded: Boolean = false,
    val lastReadPage: Int = 0
) {
    
    companion object {
        /**
         * Creates Book from metadata JSON (list response).
         * 
         * @param json JSON object containing book metadata
         * @return Book object without page content
         */
        fun fromMetadata(json: JSONObject): Book {
            return Book(
                id = json.optString("id", ""),
                title = json.optString("title", ""),
                originalLanguage = json.optString("originalLanguage", ""),
                translatedLanguage = json.optString("translatedLanguage", ""),
                difficulty = json.optString("difficulty", ""),
                createdAt = json.optString("createdAt", ""),
                pageCount = json.optInt("pageCount", 0),
                isDownloaded = false,
                lastReadPage = 0
            )
        }
        
        /**
         * Creates Book from full JSON (detail response).
         * 
         * @param json JSON object containing complete book data
         * @return Book object with page content
         */
        fun fromJson(json: JSONObject): Book {
            val pagesJson = json.optJSONArray("pages")
            val pages = mutableListOf<Page>()
            
            if (pagesJson != null) {
                for (i in 0 until pagesJson.length()) {
                    val pageJson = pagesJson.getJSONObject(i)
                    pages.add(Page.fromJson(pageJson))
                }
            }
            
            return Book(
                id = json.optString("id", ""),
                title = json.optString("title", ""),
                originalLanguage = json.optString("originalLanguage", ""),
                translatedLanguage = json.optString("translatedLanguage", ""),
                difficulty = json.optString("difficulty", ""),
                pages = pages,
                createdAt = json.optString("createdAt", ""),
                pageCount = pages.size,
                isDownloaded = true,
                lastReadPage = 0
            )
        }
    }
    
    /**
     * Returns reading progress as percentage.
     * 
     * @return Progress percentage (0-100)
     */
    fun getProgressPercentage(): Int {
        return if (pageCount > 0) {
            (lastReadPage * 100) / pageCount
        } else {
            0
        }
    }
    
    /**
     * Checks if book is completed.
     * 
     * @return True if all pages have been read
     */
    fun isCompleted(): Boolean {
        return lastReadPage >= pageCount
    }
    
    /**
     * Gets the current page to read.
     * 
     * @return Current page object or null if book is completed
     */
    fun getCurrentPage(): Page? {
        return if (lastReadPage < pages.size) {
            pages.getOrNull(lastReadPage)
        } else {
            null
        }
    }
}
