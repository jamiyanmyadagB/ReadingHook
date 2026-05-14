package com.readingnook.memoryplus.model

import org.json.JSONObject

/**
 * Data model representing a page within a book.
 * 
 * Contains original text, translated text, and page number.
 * Designed for efficient display and reading progress tracking.
 */
data class Page(
    val pageNumber: Int = 0,
    val originalText: String = "",
    val translatedText: String = "",
    val isBookmarked: Boolean = false,
    val readingTime: Long = 0L // Time in milliseconds
) {
    
    companion object {
        /**
         * Creates Page from JSON object.
         * 
         * @param json JSON object containing page data
         * @return Page object
         */
        fun fromJson(json: JSONObject): Page {
            return Page(
                pageNumber = json.optInt("pageNumber", 0),
                originalText = json.optString("originalText", ""),
                translatedText = json.optString("translatedText", ""),
                isBookmarked = false,
                readingTime = 0L
            )
        }
    }
    
    /**
     * Gets the text to display based on user preference.
     * 
     * @param showOriginal Whether to show original text
     * @return Appropriate text for display
     */
    fun getDisplayText(showOriginal: Boolean): String {
        return if (showOriginal) {
            originalText
        } else {
            translatedText
        }
    }
    
    /**
     * Estimates reading time for this page.
     * 
     * @return Estimated reading time in minutes
     */
    fun getEstimatedReadingTime(): Int {
        val wordCount = getDisplayText(false).split("\\s+".toRegex()).size
        return maxOf(1, (wordCount / 200)) // 200 words per minute average
    }
}
