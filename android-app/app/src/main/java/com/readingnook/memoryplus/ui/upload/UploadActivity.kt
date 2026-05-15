package com.readingnook.memoryplus.ui.upload

import android.app.Activity
import android.content.Intent
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.readingnook.memoryplus.databinding.ActivityUploadBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Upload activity for adding new books to ReadingNook Memory+.
 *
 * Allows users to select PDF files, extract metadata, and upload them to the library.
 * Implements file selection, PDF metadata extraction, and simulated upload progress.
 */
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private var selectedPdfUri: Uri? = null
    private var uploadJob: Job? = null
    private val languages = listOf("English", "Mongolian", "Hindi", "Korean", "Japanese", "Chinese")

    private val pickPdfLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                handlePdfSelection(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setupLanguageDropdown()
    }

    private fun setupClickListeners() {
        binding.backImageView.setOnClickListener { cancelUploadAndFinish() }
        binding.uploadAreaCard.setOnClickListener { openPdfPicker() }
        binding.uploadButton.setOnClickListener { startUploadProcess() }
        binding.cancelButton.setOnClickListener { cancelUploadAndFinish() }
        binding.cancelUploadButton.setOnClickListener { cancelUploadProcess() }
    }

    private fun setupLanguageDropdown() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, languages)
        (binding.languageAutoCompleteTextView as MaterialAutoCompleteTextView).setAdapter(adapter)
        binding.languageAutoCompleteTextView.setText(languages[0], false)
    }

    private fun openPdfPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        pickPdfLauncher.launch(Intent.createChooser(intent, "Select PDF File"))
    }

    private fun handlePdfSelection(uri: Uri) {
        selectedPdfUri = uri
        lifecycleScope.launch {
            try {
                val metadata = extractPdfMetadata(uri)
                updateFileInfo(metadata)
                enableInputFields()
                binding.bookTitleEditText.setText(metadata.fileName.removeSuffix(".pdf"))
                binding.uploadButton.isEnabled = true
            } catch (e: Exception) {
                Toast.makeText(this@UploadActivity, "Error reading PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun extractPdfMetadata(uri: Uri): PdfMetadata = withContext(Dispatchers.IO) {
        var pageCount = 0
        var fileSize = 0L
        var fileName = "unknown.pdf"
        try {
            contentResolver.openFileDescriptor(uri, "r")?.use { pfd ->
                val file = File(uri.path ?: "")
                fileSize = if (file.exists()) file.length() else pfd.statSize
                fileName = getFileName(uri) ?: "unknown.pdf"
                val renderer = PdfRenderer(pfd)
                pageCount = renderer.pageCount
                renderer.close()
            }
        } catch (e: Exception) {
            pageCount = 0
        }
        PdfMetadata(fileName, fileSize, pageCount)
    }

    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex >= 0) fileName = cursor.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun updateFileInfo(metadata: PdfMetadata) {
        binding.fileInfoLayout.visibility = View.VISIBLE
        binding.fileNameTextView.text = metadata.fileName
        binding.fileSizeTextView.text = formatFileSize(metadata.fileSize)
        binding.pageCountTextView.text = if (metadata.pageCount > 0) {
            "Estimated ${metadata.pageCount} pages"
        } else {
            "Page count unknown"
        }
    }

    private fun formatFileSize(bytes: Long): String {
        return when {
            bytes < 1024 -> "$bytes B"
            bytes < 1024 * 1024 -> "${bytes / 1024} KB"
            bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
            else -> "${bytes / (1024 * 1024 * 1024)} GB"
        }
    }

    private fun enableInputFields() {
        binding.bookTitleLayout.isEnabled = true
        binding.languageLayout.isEnabled = true
    }

    private fun startUploadProcess() {
        val bookTitle = binding.bookTitleEditText.text.toString().trim()
        if (bookTitle.isEmpty()) {
            Toast.makeText(this, "Please enter a book title", Toast.LENGTH_SHORT).show()
            return
        }
        if (selectedPdfUri == null) {
            Toast.makeText(this, "Please select a PDF file", Toast.LENGTH_SHORT).show()
            return
        }
        disableUploadUI()
        binding.progressLayout.visibility = View.VISIBLE
        uploadJob = lifecycleScope.launch {
            simulateUploadProgress(bookTitle)
        }
    }

    private suspend fun simulateUploadProgress(bookTitle: String) {
        val steps = listOf(
            "Extracting text..." to 20,
            "Detecting language..." to 40,
            "Translating content..." to 60,
            "Processing pages..." to 80,
            "Saving book..." to 100
        )
        try {
            for ((status, progress) in steps) {
                if (!currentCoroutineContext().isActive) return
                withContext(Dispatchers.Main) {
                    binding.progressTextView.text = status
                    binding.progressIndicator.setProgress(progress, true)
                }
                delay(1000)
            }
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, "Book uploaded successfully!", Toast.LENGTH_SHORT).show()
                navigateToMain()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@UploadActivity, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
                resetUploadUI()
            }
        }
    }

    private fun cancelUploadProcess() {
        uploadJob?.cancel()
        uploadJob = null
        resetUploadUI()
        Toast.makeText(this, "Upload cancelled", Toast.LENGTH_SHORT).show()
    }

    private fun disableUploadUI() {
        binding.uploadAreaCard.isEnabled = false
        binding.bookTitleLayout.isEnabled = false
        binding.languageLayout.isEnabled = false
        binding.uploadButton.isEnabled = false
        binding.cancelButton.isEnabled = false
    }

    private fun resetUploadUI() {
        binding.uploadAreaCard.isEnabled = true
        binding.bookTitleLayout.isEnabled = true
        binding.languageLayout.isEnabled = true
        binding.uploadButton.isEnabled = true
        binding.cancelButton.isEnabled = true
        binding.progressLayout.visibility = View.GONE
        binding.progressIndicator.setProgress(0, false)
        binding.progressTextView.text = ""
    }

    private fun cancelUploadAndFinish() {
        uploadJob?.cancel()
        finish()
    }

    private fun navigateToMain() {
        val intent = Intent(this, com.readingnook.memoryplus.ui.main.MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    override fun onBackPressed() { cancelUploadAndFinish() }

    override fun onDestroy() {
        super.onDestroy()
        uploadJob?.cancel()
    }

    private data class PdfMetadata(
        val fileName: String,
        val fileSize: Long,
        val pageCount: Int
    )
}