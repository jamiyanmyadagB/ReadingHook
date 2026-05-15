package com.readingnook.memoryplus.repository

import androidx.room.*
import com.readingnook.memoryplus.model.Book
import kotlinx.coroutines.flow.Flow

/**
 * Room database repository for Book entities.
 * 
 * Provides data access methods for local book storage.
 * Uses coroutines for asynchronous database operations.
 */
@Dao
interface BookRepository {

    /**
     * Inserts a new book into the database.
     * 
     * @param book Book to insert
     * @return Row ID of inserted book
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(book: Book): Long

    /**
     * Inserts multiple books into the database.
     * 
     * @param books List of books to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(books: List<Book>)

    /**
     * Retrieves all books ordered by creation date.
     * 
     * @return Flow of books sorted by newest first
     */
    @Query("SELECT * FROM books ORDER BY createdAt DESC")
    fun getAllBooks(): Flow<List<Book>>

    /**
     * Retrieves books that have been downloaded.
     * 
     * @return Flow of downloaded books
     */
    @Query("SELECT * FROM books WHERE isDownloaded = 1 ORDER BY createdAt DESC")
    fun getDownloadedBooks(): Flow<List<Book>>

    /**
     * Retrieves a specific book by ID.
     * 
     * @param bookId ID of the book
     * @return Book if found, null otherwise
     */
    @Query("SELECT * FROM books WHERE id = :bookId")
    suspend fun getBookById(bookId: String): Book?

    /**
     * Updates reading progress for a book.
     * 
     * @param bookId ID of the book
     * @param lastReadPage Current page being read
     * @return Number of rows affected
     */
    @Query("UPDATE books SET lastReadPage = :lastReadPage WHERE id = :bookId")
    suspend fun updateReadingProgress(bookId: String, lastReadPage: Int): Int

    /**
     * Updates download status for a book.
     * 
     * @param bookId ID of the book
     * @param isDownloaded Whether the book is downloaded
     * @return Number of rows affected
     */
    @Query("UPDATE books SET isDownloaded = :isDownloaded WHERE id = :bookId")
    suspend fun updateDownloadStatus(bookId: String, isDownloaded: Boolean): Int

    /**
     * Deletes a specific book.
     * 
     * @param bookId ID of the book to delete
     * @return Number of rows affected
     */
    @Query("DELETE FROM books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String): Int

    /**
     * Deletes all books.
     * 
     * @return Number of rows affected
     */
    @Query("DELETE FROM books")
    suspend fun deleteAllBooks(): Int

    /**
     * Gets reading statistics.
     * 
     * @return Flow of reading statistics
     */
    @Query("""
        SELECT 
            COUNT(*) as totalBooks,
            COUNT(CASE WHEN completed = 1 THEN 1 END) as completedBooks,
            SUM(lastReadPage) as totalPagesRead
        FROM books
        WHERE isDownloaded = 1
    """)
    fun getReadingStats(): Flow<ReadingStats>

    /**
     * Data class for reading statistics.
     */
    data class ReadingStats(
        val totalBooks: Int,
        val completedBooks: Int,
        val totalPagesRead: Int
    )
}