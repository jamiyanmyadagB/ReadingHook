package com.readingnook.memoryplus.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.readingnook.memoryplus.databinding.BottomSheetSignupBinding

/**
 * Sign up bottom sheet for user registration.
 * 
 * Provides name, email, password, and confirm password fields
 * with real-time validation.
 */
class SignUpBottomSheet : BottomSheetDialogFragment() {
    
    private var _binding: BottomSheetSignupBinding? = null
    private val binding get() = _binding!!
    
    private var onSignUpClickListener: ((String, String, String, String) -> Unit)? = null
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSignupBinding.inflate(inflater, container, false)
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
        // Name validation
        binding.nameEditText.addTextChangedListener { text ->
            val isValid = text.toString().length >= 2
            updateSignUpButtonState()
        }
        
        // Email validation
        binding.emailEditText.addTextChangedListener { text ->
            val isValid = isValidEmail(text.toString())
            updateSignUpButtonState()
        }
        
        // Password validation
        binding.passwordEditText.addTextChangedListener { text ->
            val isValid = text.toString().length >= 6
            updateSignUpButtonState()
        }
        
        // Confirm password validation
        binding.confirmPasswordEditText.addTextChangedListener { text ->
            updateSignUpButtonState()
        }
    }
    
    /**
     * Updates sign up button state based on input validation.
     */
    private fun updateSignUpButtonState() {
        val nameValid = binding.nameEditText.text.toString().length >= 2
        val emailValid = isValidEmail(binding.emailEditText.text.toString())
        val passwordValid = binding.passwordEditText.text.toString().length >= 6
        val passwordMatch = binding.passwordEditText.text.toString() == 
                          binding.confirmPasswordEditText.text.toString()
        
        binding.signUpButton.isEnabled = nameValid && emailValid && passwordValid && passwordMatch
        binding.signUpButton.alpha = if (nameValid && emailValid && passwordValid && passwordMatch) 1.0f else 0.5f
    }
    
    /**
     * Sets up click listeners for form interactions.
     */
    private fun setupClickListeners() {
        // Sign up button
        binding.signUpButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            
            if (validateInputs(name, email, password, confirmPassword)) {
                onSignUpClickListener?.invoke(name, email, password)
                dismiss()
                // Navigate to MainActivity after successful sign-up
                navigateToMain()
            }
        }
        
        // Close button
        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }
    
    /**
     * Navigates to MainActivity after successful sign-up.
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
    private fun validateInputs(
        name: String, 
        email: String, 
        password: String, 
        confirmPassword: String
    ): Boolean {
        return name.isNotEmpty() && 
               email.isNotEmpty() && 
               password.isNotEmpty() && 
               confirmPassword.isNotEmpty() &&
               name.length >= 2 &&
               isValidEmail(email) &&
               password.length >= 6 &&
               password == confirmPassword
    }
    
    /**
     * Sets sign up click listener.
     */
    fun setOnSignUpClickListener(listener: (String, String, String, String) -> Unit) {
        onSignUpClickListener = listener
    }
}
