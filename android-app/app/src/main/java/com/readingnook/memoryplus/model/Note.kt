package com.readingnook.memoryplus.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONObject

/**
 * Data model representing a vocabulary note.
 * 
 * Contains word selections, translations, and explanations.
 * Designed for efficient vocabulary building and review.
 */
@Entity(tableName = "notes")
data class Note(
    @PrimaryKey
    val id: String = "",
    val bookId: String = "",
    val selectedWord: String = "",
    val originalText: String = "",
    val translatedText: String = "",
    val hinglishExplanation: String = "",
    val difficulty: String = "",
    val createdAt: String = "",
    val isFavorite: Boolean = false,
    val reviewCount: Int = 0,
    val lastReviewDate: String = ""
) {
    
    companion object {
        /**
         * Creates Note from JSON object.
         * 
         * @param json JSON object containing note data
         * @return Note object
         */
        fun fromJson(json: JSONObject): Note {
            return Note(
                id = json.optString("id", ""),
                bookId = json.optString("bookId", ""),
                selectedWord = json.optString("selectedWord", ""),
                originalText = json.optString("originalText", ""),
                translatedText = json.optString("translatedText", ""),
                hinglishExplanation = json.optString("hinglishExplanation", ""),
                difficulty = json.optString("difficulty", ""),
                createdAt = json.optString("createdAt", ""),
                isFavorite = json.optBoolean("isFavorite", false),
                reviewCount = json.optInt("reviewCount", 0),
                lastReviewDate = json.optString("lastReviewDate", "")
            )
        }
    }
    
    /**
     * Converts Note to JSON for API requests.
     * 
     * @return JSON representation of note
     */
    fun toJson(): String {
        val json = JSONObject()
        json.put("bookId", bookId)
        json.put("selectedWord", selectedWord)
        json.put("originalText", originalText)
        json.put("translatedText", translatedText)
        json.put("hinglishExplanation", hinglishExplanation)
        json.put("difficulty", difficulty)
        
        return json.toString()
    }
    
    /**
     * Gets difficulty color for UI display.
     * 
     * @return Color resource ID based on difficulty
     */
    fun getDifficultyColor(): String {
        return when (difficulty.lowercase()) {
            "easy" -> "#4CAF50" // Green
            "medium" -> "#FF9800" // Orange
            "hard" -> "#F44336" // Red
            else -> "#757575" // Grey
        }
    }
    
    /**
     * Checks if note needs review based on spaced repetition.
     * 
     * @return True if note should be reviewed
     */
    fun needsReview(): Boolean {
        if (lastReviewDate.isEmpty()) {
            return true
        }
        
        // Simple algorithm: review if not reviewed in 7 days
        val lastReview = lastReviewDate.toLongOrNull() ?: 0L
        val now = System.currentTimeMillis()
        val sevenDaysInMs = 7 * 24 * 60 * 60 * 1000L
        
        return (now - lastReview) > sevenDaysInMs
    }
}
