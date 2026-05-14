package com.readingnook.memoryplus.ui.notes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.readingnook.memoryplus.adapter.NoteAdapter
import com.readingnook.memoryplus.databinding.ActivityNotesBinding
import com.readingnook.memoryplus.viewmodel.NoteViewModel

/**
 * Notes activity for displaying and managing vocabulary notes.
 * 
 * Features expandable note cards, difficulty filtering, and swipe-to-delete.
 * Implements MVVM pattern with NoteViewModel.
 */
class NotesActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityNotesBinding
    private lateinit var noteAdapter: NoteAdapter
    private val noteViewModel: NoteViewModel by viewModels()
    
    private var currentFilter = "all"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Setup UI components
        setupRecyclerView()
        setupFilterChips()
        setupClickListeners()
        setupBottomNavigation()
        
        // Observe ViewModel data
        observeViewModel()
        
        // Load initial data
        noteViewModel.loadNotes()
    }
    
    /**
     * Sets up RecyclerView with expandable adapter.
     */
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter()
        
        binding.notesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@NotesActivity)
            adapter = noteAdapter
            
            // Add swipe-to-delete functionality
            setupSwipeToDelete()
        }
        
        // Set note click listener
        noteAdapter.setOnNoteClickListener { note ->
            // Handle note click (could open detail view)
            showNoteDetails(note)
        }
    }
    
    /**
     * Sets up swipe-to-delete functionality.
     */
    private fun setupSwipeToDelete() {
        val swipeCallback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
            0,
            androidx.recyclerview.widget.ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                val note = noteAdapter.getNoteAt(position)
                
                note?.let {
                    deleteNote(it)
                }
            }
            
            override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                return 0.5f
            }
        }
        
        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(binding.notesRecyclerView)
    }
    
    /**
     * Sets up filter chips for difficulty filtering.
     */
    private fun setupFilterChips() {
        // All chip
        binding.allChip.setOnClickListener {
            selectFilterChip("all")
            noteViewModel.filterNotes("all")
        }
        
        // Easy chip
        binding.easyChip.setOnClickListener {
            selectFilterChip("easy")
            noteViewModel.filterNotes("easy")
        }
        
        // Medium chip
        binding.mediumChip.setOnClickListener {
            selectFilterChip("medium")
            noteViewModel.filterNotes("medium")
        }
        
        // Hard chip
        binding.hardChip.setOnClickListener {
            selectFilterChip("hard")
            noteViewModel.filterNotes("hard")
        }
        
        // Select "All" by default
        selectFilterChip("all")
    }
    
    /**
     * Selects a filter chip and updates UI.
     */
    private fun selectFilterChip(filter: String) {
        currentFilter = filter
        
        // Reset all chips to unselected state
        listOf(binding.allChip, binding.easyChip, binding.mediumChip, binding.hardChip)
            .forEach { chip ->
                chip.setChipBackgroundColorResource(com.readingnook.memoryplus.R.color.background_card)
                chip.setTextColor(getColor(com.readingnook.memoryplus.R.color.text_primary))
                chip.chipStrokeColorResource = com.readingnook.memoryplus.R.color.divider
                chip.chipStrokeWidth = 1
            }
        
        // Highlight selected chip
        val selectedChip = when (filter) {
            "all" -> binding.allChip
            "easy" -> binding.easyChip
            "medium" -> binding.mediumChip
            "hard" -> binding.hardChip
            else -> binding.allChip
        }
        
        selectedChip.setChipBackgroundColorResource(com.readingnook.memoryplus.R.color.primary)
        selectedChip.setTextColor(getColor(com.readingnook.memoryplus.R.color.text_on_primary))
        selectedChip.chipStrokeWidth = 0
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
                    // Navigate to profile
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
