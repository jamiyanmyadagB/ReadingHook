package com.readingnook.memoryplus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingnook.memoryplus.model.Book
import com.readingnook.memoryplus.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

/**
 * ViewModel for managing book data in ReadingNook Memory+.
 * 
 * Handles book loading, searching, and state management.
 * Uses coroutines for asynchronous operations and LiveData for reactive UI updates.
 */
class BookViewModel(
    private val bookRepository: BookRepository
) : ViewModel() {
    
    private val TAG = "BookViewModel"
    
    // LiveData for books list
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books
    
    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // StateFlow for search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    // StateFlow for filtered books
    private val _filteredBooks = MutableStateFlow<List<Book>>(emptyList())
    val filteredBooks: StateFlow<List<Book>> = _filteredBooks.asStateFlow()
    
    init {
        // Observe search query changes
        viewModelScope.launch {
            searchQuery.collect { query ->
                filterBooks(query)
            }
        }
    }
    
    /**
     * Loads all books from repository.
     * Fetches from API and updates LiveData.
     */
    fun loadBooks() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""
                
                Log.d(TAG, "Loading books from repository")
                
                // Fetch books from repository (collect from Flow)
                bookRepository.getAllBooks().collect { booksList ->
                    _books.postValue(booksList)
                    _filteredBooks.value = booksList
                    Log.d(TAG, "Loaded ${booksList.size} books")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error loading books", e)
                _errorMessage.value = "Failed to load books: ${e.message}"
                _books.postValue(emptyList())
                _filteredBooks.value = emptyList()
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Searches books based on query.
     * Updates filtered books StateFlow.
     */
    fun searchBooks(query: String) {
        _searchQuery.value = query
    }
    
    /**
     * Filters books based on search query.
     * Updates filtered books StateFlow.
     */
    private fun filterBooks(query: String) {
        viewModelScope.launch {
            val currentBooks = _books.value ?: emptyList()
            
            val filtered = if (query.isBlank()) {
                currentBooks
            } else {
                currentBooks.filter { book ->
                    book.title.contains(query, ignoreCase = true) ||
                    book.originalLanguage.contains(query, ignoreCase = true) ||
                    book.translatedLanguage.contains(query, ignoreCase = true) ||
                    book.difficulty.contains(query, ignoreCase = true)
                }
            }
            
            _filteredBooks.value = filtered
            Log.d(TAG, "Filtered ${filtered.size} books from query: '$query'")
        }
    }
    
    /**
     * Refreshes books list.
     * Forces reload from API.
     */
    fun refreshBooks() {
        Log.d(TAG, "Refreshing books list")
        loadBooks()
    }
    
    /**
     * Gets a specific book by ID.
     */
    fun getBookById(bookId: String): LiveData<Book?> {
        val result = MutableLiveData<Book?>()
        
        viewModelScope.launch {
            try {
                val book = withContext(Dispatchers.IO) {
                    bookRepository.getBookById(bookId)
                }
                result.postValue(book)
            } catch (e: Exception) {
                Log.e(TAG, "Error getting book by ID: $bookId", e)
                result.postValue(null)
            }
        }
        
        return result
    }
    
    /**
     * Updates reading progress for a book.
     */
    fun updateReadingProgress(bookId: String, currentPage: Int) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    bookRepository.updateReadingProgress(bookId, currentPage)
                }
                
                // Refresh books to show updated progress
                loadBooks()
                
                Log.d(TAG, "Updated reading progress for book: $bookId, page: $currentPage")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error updating reading progress", e)
                _errorMessage.value = "Failed to update reading progress: ${e.message}"
            }
        }
    }
    
    /**
     * Deletes a book from the library.
     */
    fun deleteBook(bookId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                withContext(Dispatchers.IO) {
                    bookRepository.deleteBook(bookId)
                }
                
                // Refresh books list
                loadBooks()
                
                Log.d(TAG, "Deleted book: $bookId")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting book", e)
                _errorMessage.value = "Failed to delete book: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Clears error message.
     */
    fun clearError() {
        _errorMessage.value = ""
    }
    
    /**
     * Gets reading statistics.
     */
    fun getReadingStats(): LiveData<com.readingnook.memoryplus.repository.BookRepository.ReadingStats> {
        val result = MutableLiveData<com.readingnook.memoryplus.repository.BookRepository.ReadingStats>()
        
        viewModelScope.launch {
            try {
                bookRepository.getReadingStats().collect { stats ->
                    result.postValue(stats)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting reading stats", e)
                result.postValue(com.readingnook.memoryplus.repository.BookRepository.ReadingStats(0, 0, 0))
            }
        }
        
        return result
    }
    
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "BookViewModel cleared")
    }
}
