package com.readingnook.memoryplus.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.readingnook.memoryplus.R
import com.readingnook.memoryplus.databinding.DialogForgotPasswordBinding

/**
 * Forgot password dialog for password reset.
 * 
 * Provides email input field and reset functionality.
 */
class ForgotPasswordDialog : androidx.fragment.app.DialogFragment() {
    
    private var _binding: DialogForgotPasswordBinding? = null
    private val binding get() = _binding!!
    
    private var onSendClickListener: ((String) -> Unit)? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupInputValidation()
        setupClickListeners()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    /**
     * Sets up real-time email validation.
     */
    private fun setupInputValidation() {
        binding.emailEditText.addTextChangedListener { text ->
            val isValid = isValidEmail(text.toString())
            updateSendButtonState(isValid)
        }
    }
    
    /**
     * Updates send button state based on email validation.
     */
    private fun updateSendButtonState(isValid: Boolean) {
        binding.sendButton.isEnabled = isValid
        binding.sendButton.alpha = if (isValid) 1.0f else 0.5f
    }
    
    /**
     * Sets up click listeners for dialog interactions.
     */
    private fun setupClickListeners() {
        // Send button
        binding.sendButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            
            if (validateEmail(email)) {
                onSendClickListener?.invoke(email)
                dismiss()
            }
        }
        
        // Cancel button
        binding.cancelTextView.setOnClickListener {
            dismiss()
        }
    }
    
    /**
     * Validates email format.
     */
    private fun isValidEmail(email: String): Boolean {
        return email.isNotEmpty() && 
               android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Sets send reset click listener.
     */
    fun setOnSendClickListener(listener: (String) -> Unit) {
        onSendClickListener = listener
    }
}
