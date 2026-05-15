package com.readingnook.memoryplus.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.readingnook.memoryplus.model.Note
import com.readingnook.memoryplus.repository.NoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

/**
 * ViewModel for managing vocabulary notes in ReadingNook Memory+.
 * 
 * Handles note loading, filtering, deletion, and state management.
 * Uses coroutines for asynchronous operations and LiveData for reactive UI updates.
 */
class NoteViewModel(
    private val noteRepository: NoteRepository
) : ViewModel() {
    
    private val TAG = "NoteViewModel"
    
    // LiveData for notes list
    private val _notes = MutableLiveData<List<Note>>()
    val notes: LiveData<List<Note>> = _notes
    
    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // StateFlow for filter query
    private val _filterQuery = MutableStateFlow("all")
    val filterQuery: StateFlow<String> = _filterQuery.asStateFlow()
    
    // StateFlow for filtered notes
    private val _filteredNotes = MutableStateFlow<List<Note>>(emptyList())
    val filteredNotes: StateFlow<List<Note>> = _filteredNotes.asStateFlow()
    
    // List for deleted notes (for undo functionality)
    private val deletedNotes = mutableListOf<Note>()
    
    init {
        // Observe filter query changes
        viewModelScope.launch {
            filterQuery.collect { query ->
                applyFilters(query)
            }
        }
    }
    
    /**
     * Loads all notes from repository.
     * Fetches from local database and updates LiveData.
     */
    fun loadNotes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""
                
                Log.d(TAG, "Loading notes from repository")
                
                // Fetch notes from repository (collect from Flow)
                noteRepository.getAllNotes().collect { notesList ->
                    _notes.postValue(notesList)
                    _filteredNotes.value = notesList
                    Log.d(TAG, "Loaded ${notesList.size} notes")
                    _isLoading.value = false
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error loading notes", e)
                _errorMessage.value = "Failed to load notes: ${e.message}"
                _notes.postValue(emptyList())
                _filteredNotes.value = emptyList()
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Filters notes based on difficulty.
     * Updates filtered notes StateFlow.
     */
    fun filterNotes(filter: String) {
        _filterQuery.value = filter
    }
    
    /**
     * Applies filters based on current filter query.
     * Updates filtered notes StateFlow.
     */
    private fun applyFilters(filter: String) {
        viewModelScope.launch {
            val currentNotes = _notes.value ?: emptyList()
            
            val filtered = when (filter.lowercase()) {
                "all" -> currentNotes
                "easy" -> currentNotes.filter { it.difficulty.lowercase() == "easy" }
                "medium" -> currentNotes.filter { it.difficulty.lowercase() == "medium" }
                "hard" -> currentNotes.filter { it.difficulty.lowercase() == "hard" }
                else -> currentNotes
            }
            
            _filteredNotes.value = filtered
            Log.d(TAG, "Filtered ${filtered.size} notes with filter: '$filter'")
        }
    }
    
    /**
     * Deletes a note from the repository.
     */
    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                // Find note to delete
                val noteToDelete = _notes.value?.find { it.id == noteId }
                
                if (noteToDelete != null) {
                    // Add to deleted notes for undo
                    deletedNotes.add(noteToDelete)
                    
                    withContext(Dispatchers.IO) {
                        noteRepository.deleteNote(noteId)
                    }
                    
                    // Refresh notes list
                    loadNotes()
                    
                    Log.d(TAG, "Deleted note: $noteId")
                } else {
                    _errorMessage.value = "Note not found"
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting note", e)
                _errorMessage.value = "Failed to delete note: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Restores a deleted note (undo functionality).
     */
    fun restoreNote(note: Note) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                
                withContext(Dispatchers.IO) {
                    noteRepository.insert(note)
                }
                
                // Remove from deleted notes
                deletedNotes.remove(note)
                
                // Refresh notes list
                loadNotes()
                
                Log.d(TAG, "Restored note: ${note.id}")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error restoring note", e)
                _errorMessage.value = "Failed to restore note: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Gets notes by book ID.
     */
    fun getNotesByBookId(bookId: String): LiveData<List<Note>> {
        val result = MutableLiveData<List<Note>>()
        
        viewModelScope.launch {
            try {
                noteRepository.getNotesByBookId(bookId).collect { notes ->
                    result.postValue(notes)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting notes by book ID: $bookId", e)
                result.postValue(emptyList())
            }
        }
        
        return result
    }
    
    /**
     * Gets favorite notes.
     */
    fun getFavoriteNotes(): LiveData<List<Note>> {
        val result = MutableLiveData<List<Note>>()
        
        viewModelScope.launch {
            try {
                noteRepository.getFavoriteNotes().collect { notes ->
                    result.postValue(notes)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting favorite notes", e)
                result.postValue(emptyList())
            }
        }
        
        return result
    }
    
    /**
     * Updates note favorite status.
     */
    fun updateNoteFavorite(noteId: String, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    noteRepository.updateFavoriteStatus(noteId, isFavorite)
                }
                
                // Refresh notes list
                loadNotes()
                
                Log.d(TAG, "Updated favorite status for note: $noteId")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error updating note favorite", e)
                _errorMessage.value = "Failed to update note favorite: ${e.message}"
            }
        }
    }
    
    /**
     * Gets notes that need review.
     */
    fun getNotesNeedingReview(): LiveData<List<Note>> {
        val result = MutableLiveData<List<Note>>()
        
        viewModelScope.launch {
            try {
                noteRepository.getNotesNeedingReview().collect { notes ->
                    result.postValue(notes)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting notes needing review", e)
                result.postValue(emptyList())
            }
        }
        
        return result
    }
    
    /**
     * Gets note statistics.
     */
    fun getNoteStats(): LiveData<com.readingnook.memoryplus.repository.NoteRepository.NoteStats> {
        val result = MutableLiveData<com.readingnook.memoryplus.repository.NoteRepository.NoteStats>()
        
        viewModelScope.launch {
            try {
                noteRepository.getNoteStats().collect { stats ->
                    result.postValue(stats)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error getting note stats", e)
                result.postValue(com.readingnook.memoryplus.repository.NoteRepository.NoteStats(0, 0, 0, 0, 0))
            }
        }
        
        return result
    }
    
    /**
     * Clears error message.
     */
    fun clearError() {
        _errorMessage.value = ""
    }
    
    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "NoteViewModel cleared")
    }
}
