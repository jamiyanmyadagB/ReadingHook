package com.readingnook.memoryplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.readingnook.memoryplus.databinding.ItemBookBinding
import com.readingnook.memoryplus.model.Book

/**
 * RecyclerView adapter for displaying books in a grid layout.
 * 
 * Uses DiffUtil for efficient list updates and Material3 design.
 * Handles book selection and progress display.
 */
class BookAdapter : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {
    
    private var onBookClickListener: ((Book) -> Unit)? = null
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    /**
     * ViewHolder for book items.
     */
    inner class BookViewHolder(
        private val binding: ItemBookBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(book: Book) {
            // Set book title
            binding.titleTextView.text = book.title
            
            // Set language badge
            binding.languageTextView.text = book.translatedLanguage.take(2).uppercase()
            
            // Set difficulty chip
            setupDifficultyChip(book.difficulty)
            
            // Set reading progress
            setupProgress(book)
            
            // Set spine color based on difficulty
            setupSpineColor(book.difficulty)
            
            // Set click listener
            binding.root.setOnClickListener {
                onBookClickListener?.invoke(book)
            }
        }
        
        private fun setupDifficultyChip(difficulty: String) {
            binding.difficultyChip.text = difficulty
            
            val (backgroundColor, textColor) = when (difficulty.lowercase()) {
                "easy" -> Pair(binding.root.context.getColor(com.readingnook.memoryplus.R.color.difficulty_easy),
                              binding.root.context.getColor(com.readingnook.memoryplus.R.color.text_on_primary))
                "medium" -> Pair(binding.root.context.getColor(com.readingnook.memoryplus.R.color.difficulty_medium),
                                binding.root.context.getColor(com.readingnook.memoryplus.R.color.text_on_primary))
                "hard" -> Pair(binding.root.context.getColor(com.readingnook.memoryplus.R.color.difficulty_hard),
                              binding.root.context.getColor(com.readingnook.memoryplus.R.color.text_on_primary))
                else -> Pair(binding.root.context.getColor(com.readingnook.memoryplus.R.color.primary),
                             binding.root.context.getColor(com.readingnook.memoryplus.R.color.text_on_primary))
            }
            
            binding.difficultyChip.setChipBackgroundColorResource(
                when (difficulty.lowercase()) {
                    "easy" -> com.readingnook.memoryplus.R.color.difficulty_easy
                    "medium" -> com.readingnook.memoryplus.R.color.difficulty_medium
                    "hard" -> com.readingnook.memoryplus.R.color.difficulty_hard
                    else -> com.readingnook.memoryplus.R.color.primary
                }
            )
        }
        
        private fun setupProgress(book: Book) {
            val progress = book.getProgressPercentage()
            val totalPages = book.pages.size
            val currentPage = book.lastReadPage
            
            // Set progress text
            binding.progressTextView.text = "$currentPage/$totalPages pages"
            binding.percentageTextView.text = "$progress%"
            
            // Set progress bar
            binding.progressBar.progress = progress
            
            // Show/hide progress section based on whether book has been started
            if (currentPage > 0) {
                binding.progressBar.visibility = android.view.View.VISIBLE
                binding.progressTextView.visibility = android.view.View.VISIBLE
                binding.percentageTextView.visibility = android.view.View.VISIBLE
            } else {
                binding.progressBar.visibility = android.view.View.GONE
                binding.progressTextView.visibility = android.view.View.GONE
                binding.percentageTextView.visibility = android.view.View.GONE
            }
        }
        
        private fun setupSpineColor(difficulty: String) {
            val spineColor = when (difficulty.lowercase()) {
                "easy" -> com.readingnook.memoryplus.R.color.difficulty_easy
                "medium" -> com.readingnook.memoryplus.R.color.difficulty_medium
                "hard" -> com.readingnook.memoryplus.R.color.difficulty_hard
                else -> com.readingnook.memoryplus.R.color.primary
            }
            
            binding.spineView.setBackgroundColor(
                binding.root.context.getColor(spineColor)
            )
        }
    }
    
    /**
     * DiffUtil callback for efficient list updates.
     */
    class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
    
    /**
     * Sets book click listener.
     */
    fun setOnBookClickListener(listener: (Book) -> Unit) {
        onBookClickListener = listener
    }
}
