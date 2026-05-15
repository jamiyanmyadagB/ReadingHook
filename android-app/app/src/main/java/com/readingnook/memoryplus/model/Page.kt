package com.readingnook.memoryplus.model

import android.os.Parcel
import android.os.Parcelable
import org.json.JSONObject

/**
 * Data model representing a page within a book.
 * 
 * Contains original text, translated text, and page number.
 * Designed for efficient display and reading progress tracking.
 * Implements Parcelable for safe intent passing between activities.
 */
data class Page(
    val pageNumber: Int = 0,
    val originalText: String = "",
    val translatedText: String = "",
    val isBookmarked: Boolean = false,
    val readingTime: Long = 0L // Time in milliseconds
) : Parcelable {
    
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
    
    // Parcelable implementation
    constructor(parcel: Parcel) : this(
        pageNumber = parcel.readInt(),
        originalText = parcel.readString() ?: "",
        translatedText = parcel.readString() ?: "",
        isBookmarked = parcel.readByte() != 0.toByte(),
        readingTime = parcel.readLong()
    )
    
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(pageNumber)
        parcel.writeString(originalText)
        parcel.writeString(translatedText)
        parcel.writeByte(if (isBookmarked) 1 else 0)
        parcel.writeLong(readingTime)
    }
    
    override fun describeContents(): Int {
        return 0
    }
    
    companion object CREATOR : Parcelable.Creator<Page> {
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
        
        override fun createFromParcel(parcel: Parcel): Page {
            return Page(parcel)
        }
        
        override fun newArray(size: Int): Array<Page?> {
            return arrayOfNulls(size)
        }
    }
}
