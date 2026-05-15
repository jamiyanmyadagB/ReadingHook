package com.readingnook.memoryplus.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.readingnook.memoryplus.R
import com.readingnook.memoryplus.databinding.BottomSheetLoginBinding
import com.readingnook.memoryplus.ui.main.MainActivity

/**
 * Login bottom sheet for authentication.
 * 
 * Provides email and password input fields
 * with forgot password option.
 */
class LoginBottomSheet : BottomSheetDialogFragment() {
    
    private var _binding: BottomSheetLoginBinding? = null
    private val binding get() = _binding!!
    
    private var onLoginClickListener: ((String, String) -> Unit)? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetLoginBinding.inflate(inflater, container, false)
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
     * Sets up real-time input validation.
     */
    private fun setupInputValidation() {
        // Email validation
        binding.emailEditText.addTextChangedListener { text ->
            val isValid = isValidEmail(text.toString())
            updateLoginButtonState(isValid)
        }
        
        // Password validation
        binding.passwordEditText.addTextChangedListener { text ->
            val isValid = text.toString().length >= 6
            updateLoginButtonState(isValid)
        }
    }
    
    /**
     * Updates login button state based on input validation.
     */
    private fun updateLoginButtonState(isValid: Boolean) {
        val emailValid = isValidEmail(binding.emailEditText.text.toString())
        val passwordValid = binding.passwordEditText.text.toString().length >= 6
        
        binding.loginButton.isEnabled = emailValid && passwordValid
        binding.loginButton.alpha = if (emailValid && passwordValid) 1.0f else 0.5f
    }
    
    /**
     * Sets up click listeners for form interactions.
     */
    private fun setupClickListeners() {
        // Login button
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            
            if (validateInputs(email, password)) {
                onLoginClickListener?.invoke(email, password)
                dismiss()
                // Navigate to MainActivity after successful login
                navigateToMain()
            }
        }
        
        // Forgot password link
        binding.forgotPasswordTextView.setOnClickListener {
            showForgotPasswordDialog()
        }
        
        // Close button
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }
    
    /**
     * Navigates to MainActivity after successful login.
     */
    private fun navigateToMain() {
        val intent = android.content.Intent(requireContext(), com.readingnook.memoryplus.ui.main.MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    
    /**
     * Validates email format.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    /**
     * Validates all input fields.
     */
    private fun validateInputs(email: String, password: String): Boolean {
        return email.isNotEmpty() && 
               password.isNotEmpty() && 
               isValidEmail(email) && 
               password.length >= 6
    }
    
    /**
     * Shows forgot password dialog.
     */
    private fun showForgotPasswordDialog() {
        val forgotPasswordDialog = ForgotPasswordDialog()
        forgotPasswordDialog.show(parentFragmentManager, "forgot_password_dialog")
    }
    
    /**
     * Sets login click listener.
     */
    fun setOnLoginClickListener(listener: (String, String) -> Unit) {
        onLoginClickListener = listener
    }
}
