package com.readingnook.memoryplus.repository

import androidx.room.*
import com.readingnook.memoryplus.model.Note
import kotlinx.coroutines.flow.Flow

/**
 * Room database repository for Note entities.
 * 
 * Provides data access methods for local note storage.
 * Uses coroutines for asynchronous database operations.
 */
@Dao
interface NoteRepository {

    /**
     * Inserts a new note into the database.
     * 
     * @param note Note to insert
     * @return Row ID of inserted note
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(note: Note): Long

    /**
     * Inserts multiple notes into the database.
     * 
     * @param notes List of notes to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<Note>)

    /**
     * Retrieves all notes ordered by creation date.
     * 
     * @return Flow of notes sorted by newest first
     */
    @Query("SELECT * FROM notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<Note>>

    /**
     * Retrieves notes for a specific book.
     * 
     * @param bookId ID of the book
     * @return Flow of notes for the book
     */
    @Query("SELECT * FROM notes WHERE bookId = :bookId ORDER BY createdAt DESC")
    fun getNotesByBookId(bookId: String): Flow<List<Note>>

    /**
     * Retrieves favorite notes.
     * 
     * @return Flow of favorite notes
     */
    @Query("SELECT * FROM notes WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavoriteNotes(): Flow<List<Note>>

    /**
     * Retrieves notes that need review.
     * 
     * @return Flow of notes needing review
     */
    @Query("""
        SELECT * FROM notes 
        WHERE lastReviewDate = '' 
           OR lastReviewDate < datetime('now', '-7 days')
        ORDER BY createdAt DESC
    """)
    fun getNotesNeedingReview(): Flow<List<Note>>

    /**
     * Retrieves notes by difficulty level.
     * 
     * @param difficulty Difficulty level to filter by
     * @return Flow of notes with specified difficulty
     */
    @Query("SELECT * FROM notes WHERE difficulty = :difficulty ORDER BY createdAt DESC")
    fun getNotesByDifficulty(difficulty: String): Flow<List<Note>>

    /**
     * Updates an existing note.
     * 
     * @param note Note to update
     * @return Number of rows affected
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(note: Note): Int

    /**
     * Updates favorite status of a note.
     * 
     * @param noteId ID of the note
     * @param isFavorite New favorite status
     * @return Number of rows affected
     */
    @Query("UPDATE notes SET isFavorite = :isFavorite WHERE id = :noteId")
    suspend fun updateFavoriteStatus(noteId: String, isFavorite: Boolean): Int

    /**
     * Updates review information for a note.
     * 
     * @param noteId ID of the note
     * @param reviewDate Current date as string
     * @param reviewCount New review count
     * @return Number of rows affected
     */
    @Query("UPDATE notes SET lastReviewDate = :reviewDate, reviewCount = :reviewCount WHERE id = :noteId")
    suspend fun updateReviewInfo(noteId: String, reviewDate: String, reviewCount: Int): Int

    /**
     * Deletes a specific note.
     * 
     * @param noteId ID of the note to delete
     * @return Number of rows affected
     */
    @Query("DELETE FROM notes WHERE id = :noteId")
    suspend fun deleteNote(noteId: String): Int

    /**
     * Deletes all notes for a specific book.
     * 
     * @param bookId ID of the book
     * @return Number of rows affected
     */
    @Query("DELETE FROM notes WHERE bookId = :bookId")
    suspend fun deleteNotesByBookId(bookId: String): Int

    /**
     * Deletes all notes.
     * 
     * @return Number of rows affected
     */
    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes(): Int

    /**
     * Gets note statistics.
     * 
     * @return Flow of note statistics
     */
    @Query("""
        SELECT 
            COUNT(*) as totalNotes,
            COUNT(CASE WHEN isFavorite = 1 THEN 1 END) as favoriteNotes,
            COUNT(CASE WHEN difficulty = 'easy' THEN 1 END) as easyNotes,
            COUNT(CASE WHEN difficulty = 'medium' THEN 1 END) as mediumNotes,
            COUNT(CASE WHEN difficulty = 'hard' THEN 1 END) as hardNotes
        FROM notes
    """)
    fun getNoteStats(): Flow<NoteStats>

    /**
     * Data class for note statistics.
     */
    data class NoteStats(
        val totalNotes: Int,
        val favoriteNotes: Int,
        val easyNotes: Int,
        val mediumNotes: Int,
        val hardNotes: Int
    )
}