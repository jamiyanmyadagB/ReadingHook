package com.readingnook.memoryplus.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.ViewModelProvider
import com.readingnook.memoryplus.ReadingNookApplication
import com.readingnook.memoryplus.adapter.BookAdapter
import com.readingnook.memoryplus.viewmodel.BookViewModelFactory
import com.readingnook.memoryplus.databinding.ActivityMainBinding
import com.readingnook.memoryplus.ui.upload.UploadActivity
import com.readingnook.memoryplus.viewmodel.BookViewModel

/**
 * Main activity displaying the bookshelf for ReadingNook Memory+.
 * 
 * Shows user's book collection with search, filtering, and navigation.
 * Implements MVVM pattern with BookViewModel.
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookViewModel: BookViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize ViewModel with repository
        val app = application as ReadingNookApplication
        val factory = BookViewModelFactory(app.bookRepository)
        bookViewModel = ViewModelProvider(this, factory)[BookViewModel::class.java]
        
        // Setup UI components
        setupRecyclerView()
        setupSearch()
        setupClickListeners()
        setupBottomNavigation()
        
        // Observe ViewModel data
        observeViewModel()
        
        // Load initial data
        bookViewModel.loadBooks()
    }
    
    /**
     * Sets up RecyclerView with grid layout and adapter.
     */
    private fun setupRecyclerView() {
        bookAdapter = BookAdapter()
        
        binding.booksRecyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = bookAdapter
            
            // Add item decoration for spacing
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: android.graphics.Rect,
                    view: android.view.View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    val spacing = 8
                    outRect.left = spacing
                    outRect.right = spacing
                    outRect.top = spacing
                    outRect.bottom = spacing
                }
            })
        }
        
        // Set book click listener
        bookAdapter.setOnBookClickListener { book ->
            // Navigate to reader activity with book details
            navigateToReader(book)
        }
    }
    
    /**
     * Sets up search functionality with real-time filtering.
     */
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                bookViewModel.searchBooks(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    /**
     * Sets up click listeners for UI elements.
     */
    private fun setupClickListeners() {
        // FAB click listener
        binding.uploadFab.setOnClickListener {
            navigateToUpload()
        }
        
        // Empty state upload button click listener
        binding.uploadEmptyButton.setOnClickListener {
            navigateToUpload()
        }
        
        // Profile icon click listener
        binding.profileImageView.setOnClickListener {
            navigateToProfile()
        }
    }
    
    /**
     * Sets up bottom navigation.
     */
    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.readingnook.memoryplus.R.id.nav_home -> {
                    // Already on home, no action needed
                    true
                }
                com.readingnook.memoryplus.R.id.nav_notes -> {
                    navigateToNotes()
                    true
                }
                com.readingnook.memoryplus.R.id.nav_profile -> {
                    navigateToProfile()
                    true
                }
                else -> false
            }
        }
        
        // Set home as selected by default
        binding.bottomNavigationView.selectedItemId = com.readingnook.memoryplus.R.id.nav_home
    }
    
    /**
     * Observes ViewModel LiveData and Flow.
     */
    private fun observeViewModel() {
        // Observe books list
        bookViewModel.books.observe(this) { books ->
            bookAdapter.submitList(books)
            updateEmptyStateVisibility(books.isEmpty())
            updateBookCount(books.size)
        }
        
        // Observe loading state
        bookViewModel.isLoading.observe(this) { isLoading ->
            // Update UI loading state
            updateLoadingState(isLoading)
        }
        
        // Observe errors
        bookViewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                showError(message)
            }
        }
    }
    
    /**
     * Updates empty state visibility based on book list.
     */
    private fun updateEmptyStateVisibility(isEmpty: Boolean) {
        binding.emptyStateLayout.visibility = if (isEmpty) {
            android.view.View.VISIBLE
        } else {
            android.view.View.GONE
        }
        
        binding.booksRecyclerView.visibility = if (isEmpty) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
    
    /**
     * Updates book count badge.
     */
    private fun updateBookCount(count: Int) {
        binding.bookCountTextView.text = "$count books"
    }
    
    /**
     * Updates loading state.
     */
    private fun updateLoadingState(isLoading: Boolean) {
        // Show/hide loading indicator if needed
    }
    
    /**
     * Shows error message to user.
     */
    private fun showError(message: String) {
        // Show error using Snackbar or Toast
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }
    
    /**
     * Navigates to reader activity.
     */
    private fun navigateToReader(book: com.readingnook.memoryplus.model.Book) {
        val intent = Intent(this, com.readingnook.memoryplus.ui.reader.ReaderActivity::class.java).apply {
            putExtra("BOOK", book)
            putExtra("CURRENT_PAGE", book.lastReadPage)
        }
        startActivity(intent)
    }
    
    /**
     * Navigates to upload activity.
     */
    private fun navigateToUpload() {
        val intent = Intent(this, com.readingnook.memoryplus.ui.upload.UploadActivity::class.java)
        startActivity(intent)
    }
    
    /**
     * Navigates to notes activity.
     */
    private fun navigateToNotes() {
        val intent = Intent(this, com.readingnook.memoryplus.ui.notes.NotesActivity::class.java)
        startActivity(intent)
    }
    
    /**
     * Navigates to profile activity.
     */
    private fun navigateToProfile() {
        // Navigate to profile activity (to be implemented)
        // For now, show a toast
        android.widget.Toast.makeText(this, "Profile coming soon!", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    override fun onResume() {
        super.onResume()
        // Refresh books when activity resumes
        bookViewModel.loadBooks()
    }
}
