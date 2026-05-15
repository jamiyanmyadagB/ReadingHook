package com.readingnook.memoryplus.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.readingnook.memoryplus.databinding.ItemNoteBinding
import com.readingnook.memoryplus.model.Note

/**
 * RecyclerView adapter for displaying vocabulary notes.
 * 
 * Features expandable items, swipe-to-delete, and difficulty filtering.
 * Uses DiffUtil for efficient list updates and Material3 design.
 */
class NoteAdapter : ListAdapter<Note, NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {
    
    private var onNoteClickListener: ((Note) -> Unit)? = null
    private var onNoteDeleteListener: ((Note) -> Unit)? = null
    private var expandedPositions = mutableSetOf<Int>()
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = ItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NoteViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position), position in expandedPositions)
    }
    
    /**
     * ViewHolder for note items with expandable content.
     */
    inner class NoteViewHolder(
        private val binding: ItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(note: Note, isExpanded: Boolean) {
            // Set word and difficulty
            binding.wordTextView.text = note.selectedWord
            setupDifficultyChip(note.difficulty)
            
            // Set explanation snippet
            binding.explanationTextView.text = note.hinglishExplanation
            
            // Set book name and date
            binding.bookNameTextView.text = "Book: ${note.bookId}" // Would get actual book name
            binding.dateTextView.text = formatDate(note.createdAt)
            
            // Set expandable content
            setupExpandableContent(note, isExpanded)
            
            // Set click listeners
            setupClickListeners(note, isExpanded)
        }
        
        private fun setupDifficultyChip(difficulty: String) {
            binding.difficultyChip.text = difficulty
            
            val difficultyColor = when (difficulty.lowercase()) {
                "easy" -> com.readingnook.memoryplus.R.color.difficulty_easy
                "medium" -> com.readingnook.memoryplus.R.color.difficulty_medium
                "hard" -> com.readingnook.memoryplus.R.color.difficulty_hard
                else -> com.readingnook.memoryplus.R.color.primary
            }
            
            binding.difficultyChip.setChipBackgroundColorResource(difficultyColor)
        }
        
        private fun setupExpandableContent(note: Note, isExpanded: Boolean) {
            // Set expanded content visibility
            binding.expandableContent.visibility = if (isExpanded) {
                android.view.View.VISIBLE
            } else {
                android.view.View.GONE
            }
            
            // Set expand indicator rotation
            binding.expandIndicator.rotation = if (isExpanded) {
                180f
            } else {
                0f
            }
            
            // Set content text
            binding.originalSentenceTextView.text = note.originalText
            binding.translatedSentenceTextView.text = note.translatedText
        }
        
        private fun setupClickListeners(note: Note, isExpanded: Boolean) {
            // Card click for expand/collapse
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleExpansion(position)
                }
            }
            
            // Expand indicator click
            binding.expandIndicator.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    toggleExpansion(position)
                }
            }
        }
        
        private fun toggleExpansion(position: Int) {
            if (position in expandedPositions) {
                expandedPositions.remove(position)
            } else {
                expandedPositions.add(position)
            }
            
            // Update only this item
            notifyItemChanged(position)
        }
        
        private fun formatDate(dateString: String): String {
            // Simple date formatting - would use proper date library in production
            return try {
                val date = java.text.SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", java.util.Locale.getDefault())
                    .parse(dateString)
                
                val now = java.util.Date()
                val diff = now.time - (date?.time ?: 0)
                
                when {
                    diff < 60_000 -> "Just now"
                    diff < 3_600_000 -> "${diff / 60_000} minutes ago"
                    diff < 86_400_000 -> "${diff / 3_600_000} hours ago"
                    diff < 604_800_000 -> "${diff / 86_400_000} days ago"
                    else -> java.text.SimpleDateFormat("MMM dd", java.util.Locale.getDefault()).format(date ?: now)
                }
            } catch (e: Exception) {
                dateString
            }
        }
    }
    
    /**
     * DiffUtil callback for efficient list updates.
     */
    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    
    /**
     * Toggles expansion state for all items.
     */
    fun toggleAllExpansion() {
        if (expandedPositions.size == itemCount) {
            // Collapse all
            expandedPositions.clear()
        } else {
            // Expand all
            expandedPositions.clear()
            expandedPositions.addAll(0 until itemCount)
        }
        notifyDataSetChanged()
    }
    
    /**
     * Sets note click listener.
     */
    fun setOnNoteClickListener(listener: (Note) -> Unit) {
        onNoteClickListener = listener
    }
    
    /**
     * Sets note delete listener.
     */
    fun setOnNoteDeleteListener(listener: (Note) -> Unit) {
        onNoteDeleteListener = listener
    }
    
    /**
     * Gets note at position.
     */
    fun getNoteAt(position: Int): Note? {
        return if (position in 0 until itemCount) {
            getItem(position)
        } else {
            null
        }
    }
}