package com.readingnook.memoryplus.ui.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.readingnook.memoryplus.R
import com.readingnook.memoryplus.model.Note

/**
 * Bottom sheet for displaying detailed word information.
 *
 * Shows word meaning, explanation, example sentence, and context.
 * Allows users to save words to their vocabulary notes.
 */
class WordDetailBottomSheet : BottomSheetDialogFragment() {

    private var selectedWord: String = ""
    private var currentPageIndex: Int = 0
    private var originalContext: String = ""

    private var onSaveNoteClickListener: ((String) -> Unit)? = null

    // Views
    private lateinit var wordTextView: TextView
    private lateinit var difficultyChip: Chip
    private lateinit var closeImageView: View
    private lateinit var englishMeaningTextView: TextView
    private lateinit var hinglishExplanationTextView: TextView
    private lateinit var exampleSentenceTextView: TextView
    private lateinit var originalContextTextView: TextView
    private lateinit var saveNoteButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_word_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bind views
        wordTextView = view.findViewById(R.id.wordTextView)
        difficultyChip = view.findViewById(R.id.difficultyChip)
        closeImageView = view.findViewById(R.id.closeImageView)
        englishMeaningTextView = view.findViewById(R.id.englishMeaningTextView)
        hinglishExplanationTextView = view.findViewById(R.id.hinglishExplanationTextView)
        exampleSentenceTextView = view.findViewById(R.id.exampleSentenceTextView)
        originalContextTextView = view.findViewById(R.id.originalContextTextView)
        saveNoteButton = view.findViewById(R.id.saveNoteButton)

        // Extract arguments
        arguments?.let { args ->
            selectedWord = args.getString(ARG_WORD, "")
            currentPageIndex = args.getInt(ARG_PAGE_INDEX, 0)
            originalContext = args.getString(ARG_CONTEXT, "")
        }

        setupUI()
        setupClickListeners()
    }

    /**
     * Sets up UI with word information.
     */
    private fun setupUI() {
        // Set word title
        wordTextView.text = selectedWord

        // Set difficulty (would come from API/database)
        val difficulty = determineDifficulty(selectedWord)
        difficultyChip.text = difficulty

        val difficultyColor = when (difficulty.lowercase()) {
            "easy" -> 0xFF4CAF50.toInt() // Green
            "medium" -> 0xFFFF9800.toInt() // Orange
            "hard" -> 0xFFF44336.toInt() // Red
            else -> 0xFFD4A373.toInt() // Gold accent
        }
        difficultyChip.setChipBackgroundColorResource(android.R.color.transparent)
        difficultyChip.setChipBackgroundColorResource(difficultyColor)

        // Set word information (would come from API/database)
        setWordInformation(selectedWord)

        // Set original context
        originalContextTextView.text = originalContext
    }

    /**
     * Sets up click listeners.
     */
    private fun setupClickListeners() {
        // Close button
        closeImageView.setOnClickListener {
            dismiss()
        }

        // Save note button
        saveNoteButton.setOnClickListener {
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

        englishMeaningTextView.text = wordInfo.englishMeaning
        hinglishExplanationTextView.text = wordInfo.hinglishExplanation
        exampleSentenceTextView.text = wordInfo.exampleSentence
    }

    /**
     * Determines word difficulty based on complexity.
     */
    private fun determineDifficulty(word: String): String {
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
            bookId = "",
            selectedWord = selectedWord,
            originalText = selectedWord,
            translatedText = englishMeaningTextView.text.toString(),
            hinglishExplanation = hinglishExplanationTextView.text.toString(),
            difficulty = difficultyChip.text.toString(),
            createdAt = java.util.Date().toString(),
            context = originalContext,
            pageNumber = currentPageIndex + 1
        )

        onSaveNoteClickListener?.invoke(selectedWord)

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