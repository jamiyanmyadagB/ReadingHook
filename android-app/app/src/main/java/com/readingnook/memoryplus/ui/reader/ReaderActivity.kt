package com.readingnook.memoryplus.ui.reader

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.readingnook.memoryplus.databinding.ActivityReaderBinding
import com.readingnook.memoryplus.model.Book
import com.readingnook.memoryplus.model.Page

/**
 * Reader activity for displaying book content in ReadingNook Memory+.
 * 
 * Provides reading interface with page navigation, text selection,
 * and vocabulary note creation functionality.
 */
class ReaderActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityReaderBinding
    
    // Book and page data
    private var book: Book? = null
    private var currentPageIndex = 0
    private var showOriginal = false
    
    // Word selection
    private var selectedWord: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityReaderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Get book data from intent
        extractBookData()
        
        // Setup UI
        setupToolbar()
        setupContent()
        setupNavigation()
        setupToggle()
        setupWordSelection()
        setupFab()
        
        // Load first page
        loadCurrentPage()
    }
    
    /**
     * Extracts book data from intent extras.
     */
    private fun extractBookData() {
        book = intent.getParcelableExtra("BOOK")
        currentPageIndex = intent.getIntExtra("CURRENT_PAGE", 0)
    }
    
    /**
     * Sets up toolbar with book information.
     */
    private fun setupToolbar() {
        book?.let { book ->
            binding.bookTitleTextView.text = book.title
            
            // Setup difficulty chip
            binding.difficultyChip.text = book.difficulty
            val difficultyColor = when (book.difficulty.lowercase()) {
                "easy" -> com.readingnook.memoryplus.R.color.difficulty_easy
                "medium" -> com.readingnook.memoryplus.R.color.difficulty_medium
                "hard" -> com.readingnook.memoryplus.R.color.difficulty_hard
                else -> com.readingnook.memoryplus.R.color.primary
            }
            binding.difficultyChip.setChipBackgroundColorResource(difficultyColor)
        }
        
        // Back navigation
        binding.backImageView.setOnClickListener {
            finish()
        }
        
        // Menu options (placeholder for future functionality)
        binding.menuImageView.setOnClickListener {
            // Show reading options menu
        }
    }
    
    /**
     * Sets up content display area.
     */
    private fun setupContent() {
        // Make text selectable for word selection
        binding.contentTextView.textIsSelectable = true
        binding.contentTextView.movementMethod = LinkMovementMethod.getInstance()
    }
    
    /**
     * Sets up page navigation controls.
     */
    private fun setupNavigation() {
        // Previous page
        binding.previousImageView.setOnClickListener {
            if (currentPageIndex > 0) {
                currentPageIndex--
                loadCurrentPage()
            }
        }
        
        // Next page
        binding.nextImageView.setOnClickListener {
            book?.let { book ->
                if (currentPageIndex < book.pages.size - 1) {
                    currentPageIndex++
                    loadCurrentPage()
                }
            }
        }
        
        // Update navigation state
        updateNavigationState()
    }
    
    /**
     * Sets up original/translated text toggle.
     */
    private fun setupToggle() {
        binding.originalTextView.setOnClickListener {
            if (!showOriginal) {
                showOriginal = true
                updateToggleState()
                loadCurrentPage()
            }
        }
        
        binding.translatedTextView.setOnClickListener {
            if (showOriginal) {
                showOriginal = false
                updateToggleState()
                loadCurrentPage()
            }
        }
        
        updateToggleState()
    }
    
    /**
     * Sets up word selection functionality.
     */
    private fun setupWordSelection() {
        binding.contentTextView.setOnLongClickListener { view ->
            // Handle long press for word selection
            val selectedText = getSelectedText()
            if (selectedText.isNotEmpty()) {
                selectedWord = selectedText
                showWordDetailBottomSheet(selectedText)
            }
            true
        }
        
        // Handle text selection changes
        binding.contentTextView.setOnClickListener {
            val selectedText = getSelectedText()
            if (selectedText.isNotEmpty()) {
                selectedWord = selectedText
                showWordDetailBottomSheet(selectedText)
            }
        }
    }
    
    /**
     * Sets up save note FAB.
     */
    private fun setupFab() {
        binding.saveNoteFab.setOnClickListener {
            selectedWord?.let { word ->
                createNoteFromSelection(word)
            }
        }
    }
    
    /**
     * Loads current page content.
     */
    private fun loadCurrentPage() {
        book?.let { book ->
            if (currentPageIndex < book.pages.size) {
                val page = book.pages[currentPageIndex]
                
                // Update page indicators
                binding.pageIndicatorTextView.text = "Page ${currentPageIndex + 1} of ${book.pages.size}"
                binding.pageNumberTextView.text = "${currentPageIndex + 1}"
                
                // Update content
                val content = if (showOriginal) {
                    page.originalText
                } else {
                    page.translatedText
                }
                
                binding.contentTextView.text = content
                
                // Update navigation state
                updateNavigationState()
                
                // Save reading progress
                saveReadingProgress()
            }
        }
    }
    
    /**
     * Updates toggle button states.
     */
    private fun updateToggleState() {
        if (showOriginal) {
            binding.originalTextView.setTextColor(
                ContextCompat.getColor(this, com.readingnook.memoryplus.R.color.text_primary)
            )
            binding.translatedTextView.setTextColor(
                ContextCompat.getColor(this, com.readingnook.memoryplus.R.color.text_hint)
            )
        } else {
            binding.originalTextView.setTextColor(
                ContextCompat.getColor(this, com.readingnook.memoryplus.R.color.text_hint)
            )
            binding.translatedTextView.setTextColor(
                ContextCompat.getColor(this, com.readingnook.memoryplus.R.color.text_primary)
            )
        }
    }
    
    /**
     * Updates navigation button states.
     */
    private fun updateNavigationState() {
        book?.let { book ->
            // Previous button
            binding.previousImageView.alpha = if (currentPageIndex > 0) 1.0f else 0.5f
            binding.previousImageView.isEnabled = currentPageIndex > 0
            
            // Next button
            binding.nextImageView.alpha = if (currentPageIndex < book.pages.size - 1) 1.0f else 0.5f
            binding.nextImageView.isEnabled = currentPageIndex < book.pages.size - 1
        }
    }
    
    /**
     * Gets currently selected text.
     */
    private fun getSelectedText(): String {
        val selectionStart = binding.contentTextView.selectionStart
        val selectionEnd = binding.contentTextView.selectionEnd
        
        return if (selectionStart >= 0 && selectionEnd > selectionStart) {
            binding.contentTextView.text.substring(selectionStart, selectionEnd).trim()
        } else {
            ""
        }
    }
    
    /**
     * Shows word detail bottom sheet.
     */
    private fun showWordDetailBottomSheet(word: String) {
        val wordDetailBottomSheet = WordDetailBottomSheet.newInstance(word, currentPageIndex)
        wordDetailBottomSheet.show(supportFragmentManager, "word_detail_bottom_sheet")
    }
    
    /**
     * Creates note from selected word.
     */
    private fun createNoteFromSelection(word: String) {
        book?.let { book ->
            val page = book.pages[currentPageIndex]
            
            // Create note with selected word and context
            val note = com.readingnook.memoryplus.model.Note(
                id = "",
                bookId = book.id,
                selectedWord = word,
                originalText = word,
                translatedText = "", // Would be filled by API
                hinglishExplanation = "", // Would be filled by API
                difficulty = book.difficulty,
                createdAt = java.util.Date().toString()
            )
            
            // Save note (would use ViewModel/Repository)
            // For now, show confirmation
            android.widget.Toast.makeText(this, "Note saved for: $word", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    /**
     * Saves reading progress.
     */
    private fun saveReadingProgress() {
        book?.let { book ->
            // Update reading progress (would use ViewModel/Repository)
            // For now, just log the progress
            android.util.Log.d("ReaderActivity", "Progress: Page ${currentPageIndex + 1}/${book.pages.size}")
        }
    }
    
    override fun onBackPressed() {
        // Return to main activity
        super.onBackPressed()
        finish()
    }
}
