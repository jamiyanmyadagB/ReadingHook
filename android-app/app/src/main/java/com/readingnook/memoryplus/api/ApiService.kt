package com.readingnook.memoryplus.api

import com.readingnook.memoryplus.model.Book
import com.readingnook.memoryplus.model.Note
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * API service for communicating with ReadingNook backend.
 *
 * Handles all HTTP requests including file uploads and data synchronization.
 * Uses OkHttp3 for networking and coroutines for async operations.
 */
class ApiService {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val baseUrl = "http://10.0.2.2:8080/api" // Replace with actual backend URL
    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }

    /**
     * Uploads a PDF file to the backend for processing.
     *
     * @param file PDF file to upload
     * @return Book object with translated content
     */
    suspend fun uploadBook(file: File): Result<Book> {
        return try {
            val mediaType = "application/json".toMediaTypeOrNull()
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.name,
                    RequestBody.create(mediaType, file))
                .build()

            val request = Request.Builder()
                .url("$baseUrl/books/upload")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        // Parse JSON response to Book object
                        val bookJson = JSONObject(responseBody)
                        Result.success(Book.fromJson(bookJson))
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Upload failed: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves all books metadata from the backend.
     *
     * @return List of books without page content
     */
    suspend fun getAllBooks(): Result<List<Book>> {
        return try {
            val request = Request.Builder()
                .url("$baseUrl/books/")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        // Parse JSON array to Book list
                        val booksArray = org.json.JSONArray(responseBody)
                        val books = mutableListOf<Book>()

                        for (i in 0 until booksArray.length()) {
                            val bookJson = booksArray.getJSONObject(i)
                            books.add(Book.fromMetadata(bookJson))
                        }

                        Result.success(books)
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch books: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves a specific book with full content.
     *
     * @param bookId ID of the book to retrieve
     * @return Complete book with all pages
     */
    suspend fun getBookById(bookId: String): Result<Book> {
        return try {
            val request = Request.Builder()
                .url("$baseUrl/books/$bookId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val bookJson = JSONObject(responseBody)
                        Result.success(Book.fromJson(bookJson))
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch book: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Creates a new note for a book.
     *
     * @param note Note object to create
     * @return Created note with generated ID
     */
    suspend fun createNote(note: Note): Result<Note> {
        return try {
            val json = note.toJson()
            val requestBody = RequestBody.create(
                "application/json".toMediaType(),
                json
            )

            val request = Request.Builder()
                .url("$baseUrl/notes/")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val noteJson = JSONObject(responseBody)
                        Result.success(Note.fromJson(noteJson))
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Failed to create note: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Retrieves all notes for a specific book.
     *
     * @param bookId ID of the book
     * @return List of notes sorted by creation date
     */
    suspend fun getNotesByBookId(bookId: String): Result<List<Note>> {
        return try {
            val request = Request.Builder()
                .url("$baseUrl/notes/$bookId")
                .get()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val notesArray = org.json.JSONArray(responseBody)
                        val notes = mutableListOf<Note>()

                        for (i in 0 until notesArray.length()) {
                            val noteJson = notesArray.getJSONObject(i)
                            notes.add(Note.fromJson(noteJson))
                        }

                        Result.success(notes)
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch notes: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Updates an existing note.
     *
     * @param noteId ID of the note to update
     * @param note Updated note data
     * @return Updated note
     */
    suspend fun updateNote(noteId: String, note: Note): Result<Note> {
        return try {
            val json = note.toJson()
            val requestBody = RequestBody.create(
                "application/json".toMediaType(),
                json
            )

            val request = Request.Builder()
                .url("$baseUrl/notes/note/$noteId")
                .put(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        val noteJson = JSONObject(responseBody)
                        Result.success(Note.fromJson(noteJson))
                    } else {
                        Result.failure(Exception("Empty response from server"))
                    }
                } else {
                    Result.failure(Exception("Failed to update note: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Deletes a specific note.
     *
     * @param noteId ID of the note to delete
     * @return Success/failure result
     */
    suspend fun deleteNote(noteId: String): Result<Unit> {
        return try {
            val request = Request.Builder()
                .url("$baseUrl/notes/note/$noteId")
                .delete()
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to delete note: ${response.code}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}