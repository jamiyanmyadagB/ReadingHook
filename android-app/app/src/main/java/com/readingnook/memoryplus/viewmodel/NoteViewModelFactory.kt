package com.readingnook.memoryplus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.readingnook.memoryplus.repository.NoteRepository

/**
 * Factory for creating NoteViewModel with repository dependency.
 * 
 * Allows proper dependency injection for ViewModels.
 */
class NoteViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
