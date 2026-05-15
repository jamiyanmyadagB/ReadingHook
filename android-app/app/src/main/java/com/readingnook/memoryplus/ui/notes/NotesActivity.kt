package com.readingnook.memoryplus.ui.notes

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.readingnook.memoryplus.ReadingNookApplication
import com.readingnook.memoryplus.adapter.NoteAdapter
import com.readingnook.memoryplus.databinding.ActivityNotesBinding
import com.readingnook.memoryplus.viewmodel.NoteViewModelFactory
import com.readingnook.memoryplus.viewmodel.NoteViewModel

/**
 * Notes activity for displaying and managing reading notes.
 * 
 * Shows user's notes with filtering options and navigation.
 * Implements MVVM pattern with NoteViewModel.
 */
class NotesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNotesBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize ViewModel with repository
        val app = application as ReadingNookApplication
        val factory = NoteViewModelFactory(app.noteRepository)
        noteViewModel = ViewModelProvider(this, factory)[NoteViewModel::class.java]
        
        // Setup UI components
        setupRecyclerView()
        setupClickListeners()
        setupBottomNavigation()
        setupFilterChips()
        
        // Observe ViewModel data
        observeViewModel()
        
        // Load initial data
        noteViewModel.loadNotes()
    }
    
    /**
     * Sets up RecyclerView with adapter.
     */
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = noteAdapter
        }
        
        // Set note click listener
        noteAdapter.setOnNoteClickListener { note ->
            // Handle note click
            navigateToReader(note)
        }
    }
    
    /**
     * Sets up click listeners.
     */
    private fun setupClickListeners() {
        // Filter icon (could show filter dialog)
        binding.filterImageView.setOnClickListener {
            // Show filter options dialog
            showFilterDialog()
        }
    }
    
    /**
     * Sets up bottom navigation.
     */
    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.readingnook.memoryplus.R.id.nav_home -> {
                    finish() // Return to main activity
                    true
                }
                com.readingnook.memoryplus.R.id.nav_notes -> {
                    // Already on notes, no action needed
                    true
                }
                com.readingnook.memoryplus.R.id.nav_profile -> {
                    // Navigate to profile (placeholder)
                    android.widget.Toast.makeText(this, "Profile coming soon!", android.widget.Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        
        // Set notes as selected by default
        binding.bottomNavigationView.selectedItemId = com.readingnook.memoryplus.R.id.nav_notes
    }
    
    /**
     * Observes ViewModel LiveData and Flow.
     */
    private fun observeViewModel() {
        // Observe notes list
        noteViewModel.notes.observe(this) { notes ->
            noteAdapter.submitList(notes)
            updateEmptyStateVisibility(notes.isEmpty())
        }
        
        // Observe loading state
        noteViewModel.isLoading.observe(this) { isLoading ->
            // Update UI loading state
            updateLoadingState(isLoading)
        }
        
        // Observe error messages
        noteViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage.isNotEmpty()) {
                showError(errorMessage)
            }
        }
    }
    
    /**
     * Updates empty state visibility.
     */
    private fun updateEmptyStateVisibility(isEmpty: Boolean) {
        binding.emptyStateLayout.visibility = if (isEmpty) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
        
        binding.notesRecyclerView.visibility = if (isEmpty) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
    
    /**
     * Updates loading state UI.
     */
    private fun updateLoadingState(isLoading: Boolean) {
        // Show/hide loading indicator
        if (isLoading) {
            // Could add a loading indicator here
        }
    }
    
    /**
     * Shows error message to user.
     */
    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
    
    /**
     * Shows note details (placeholder for future functionality).
     */
    private fun showNoteDetails(note: com.readingnook.memoryplus.model.Note) {
        // Could open detailed note view
        // For now, just expand/collapse the item
        val position = noteAdapter.currentList.indexOf(note)
        if (position != -1) {
            // Toggle expansion
            noteAdapter.notifyItemChanged(position)
        }
    }
    
    /**
     * Deletes a note with confirmation.
     */
    private fun deleteNote(note: com.readingnook.memoryplus.model.Note) {
        noteViewModel.deleteNote(note.id)
        
        // Show undo option
        Snackbar.make(
            binding.root,
            "Note deleted: ${note.selectedWord}",
            Snackbar.LENGTH_LONG
        ).setAction("Undo") {
            // Restore note
            noteViewModel.restoreNote(note)
        }.show()
    }
    
    /**
     * Shows filter dialog (placeholder for future functionality).
     */
    private fun showFilterDialog() {
        // Could show advanced filter options
        // For now, just show a toast
        android.widget.Toast.makeText(this, "Advanced filters coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh notes when activity resumes
        noteViewModel.loadNotes()
    }
}
