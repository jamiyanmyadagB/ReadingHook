package com.readingnook.memoryplus.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.readingnook.memoryplus.databinding.BottomSheetWordDetailBinding
import com.readingnook.memoryplus.model.Note

/**
 * Bottom sheet for displaying detailed word information.
 * 
 * Shows word meaning, explanation, example sentence, and context.
 * Allows users to save words to their vocabulary notes.
 */
class WordDetailBottomSheet : BottomSheetDialogFragment() {
    
    private var _binding: BottomSheetWordDetailBinding? = null
    private val binding get() = _binding!!
    
    private var selectedWord: String = ""
    private var currentPageIndex: Int = 0
    private var originalContext: String = ""
    
    private var onSaveNoteClickListener: ((String) -> Unit)? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWordDetailBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Extract arguments
        arguments?.let { args ->
            selectedWord = args.getString(ARG_WORD, "")
            currentPageIndex = args.getInt(ARG_PAGE_INDEX, 0)
            originalContext = args.getString(ARG_CONTEXT, "")
        }
        
        setupUI()
        setupClickListeners()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    /**
     * Sets up UI with word information.
     */
    private fun setupUI() {
        // Set word title
        binding.wordTextView.text = selectedWord
        
        // Set difficulty (would come from API/database)
        val difficulty = determineDifficulty(selectedWord)
        binding.difficultyChip.text = difficulty
        
        val difficultyColor = when (difficulty.lowercase()) {
            "easy" -> com.readingnook.memoryplus.R.color.difficulty_easy
            "medium" -> com.readingnook.memoryplus.R.color.difficulty_medium
            "hard" -> com.readingnook.memoryplus.R.color.difficulty_hard
            else -> com.readingnook.memoryplus.R.color.primary
        }
        binding.difficultyChip.setChipBackgroundColorResource(difficultyColor)
        
        // Set word information (would come from API/database)
        setWordInformation(selectedWord)
        
        // Set original context
        binding.originalContextTextView.text = originalContext
    }
    
    /**
     * Sets up click listeners.
     */
    private fun setupClickListeners() {
        // Close button
        binding.closeImageView.setOnClickListener {
            dismiss()
        }
        
        // Save note button
        binding.saveNoteButton.setOnClickListener {
            saveWordToNotes()
        }
    }
    
    /**
     * Sets word information based on selected word.
     * In production, this would come from API or local database.
     */
    private fun setWordInformation(word: String) {
        // Mock data for demonstration
        val wordInfo = getWordInfo(word)
        
        binding.englishMeaningTextView.text = wordInfo.englishMeaning
        binding.hinglishExplanationTextView.text = wordInfo.hinglishExplanation
        binding.exampleSentenceTextView.text = wordInfo.exampleSentence
    }
    
    /**
     * Determines word difficulty based on complexity.
     */
    private fun determineDifficulty(word: String): String {
        // Simple heuristic for difficulty determination
        return when {
            word.length <= 4 -> "Easy"
            word.length <= 7 -> "Medium"
            else -> "Hard"
        }
    }
    
    /**
     * Gets word information (mock implementation).
     */
    private fun getWordInfo(word: String): WordInfo {
        // Mock dictionary - in production, this would come from API
        return when (word.lowercase()) {
            "vulnerable" -> WordInfo(
                englishMeaning = "Open to attack or harm; susceptible to physical or emotional injury.",
                hinglishExplanation = "Jisko hurt karne aasaani se; koi bhi cheez jo easily damage ho sakti hai.",
                exampleSentence = "In my younger and more vulnerable years, my father gave me some advice."
            )
            "advice" -> WordInfo(
                englishMeaning = "Guidance or recommendations offered concerning future action.",
                hinglishExplanation = "Salaah ya mashwara; jo kaam ke bare mein bataya jaata hai.",
                exampleSentence = "My father gave me some advice that I've been turning over in my mind."
            )
            "criticizing" -> WordInfo(
                englishMeaning = "Expressing disapproval of someone or something based on perceived faults.",
                hinglishExplanation = "Kisi cheez ke galti pe baat karna; burai batana.",
                exampleSentence = "Whenever you feel like criticizing anyone, just remember that all the people in this world haven't had the advantages that you've had."
            )
            else -> WordInfo(
                englishMeaning = "Word definition would appear here.",
                hinglishExplanation = "Hinglish explanation would appear here.",
                exampleSentence = "Example sentence would appear here."
            )
        }
    }
    
    /**
     * Saves word to vocabulary notes.
     */
    private fun saveWordToNotes() {
        val note = Note(
            id = "",
            bookId = "", // Would be passed from ReaderActivity
            selectedWord = selectedWord,
            originalText = selectedWord,
            translatedText = binding.englishMeaningTextView.text.toString(),
            hinglishExplanation = binding.hinglishExplanationTextView.text.toString(),
            difficulty = binding.difficultyChip.text.toString(),
            createdAt = java.util.Date().toString(),
            context = originalContext,
            pageNumber = currentPageIndex + 1
        )
        
        // Save note (would use ViewModel/Repository)
        onSaveNoteClickListener?.invoke(selectedWord)
        
        // Show confirmation
        android.widget.Toast.makeText(
            requireContext(),
            "Note saved for: $selectedWord",
            android.widget.Toast.LENGTH_SHORT
        ).show()
        
        dismiss()
    }
    
    /**
     * Sets save note click listener.
     */
    fun setOnSaveNoteClickListener(listener: (String) -> Unit) {
        onSaveNoteClickListener = listener
    }
    
    /**
     * Data class for word information.
     */
    data class WordInfo(
        val englishMeaning: String,
        val hinglishExplanation: String,
        val exampleSentence: String
    )
    
    companion object {
        private const val ARG_WORD = "word"
        private const val ARG_PAGE_INDEX = "page_index"
        private const val ARG_CONTEXT = "context"
        
        /**
         * Creates new instance of WordDetailBottomSheet.
         */
        fun newInstance(word: String, pageIndex: Int, context: String = ""): WordDetailBottomSheet {
            val fragment = WordDetailBottomSheet()
            val args = Bundle().apply {
                putString(ARG_WORD, word)
                putInt(ARG_PAGE_INDEX, pageIndex)
                putString(ARG_CONTEXT, context)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
