package com.readingnook.memoryplus.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.readingnook.memoryplus.databinding.ActivityAuthBinding
import com.readingnook.memoryplus.ui.main.MainActivity

/**
 * Authentication activity for ReadingNook Memory+.
 * 
 * Provides login, sign up, and guest mode options.
 * Navigates to main app after authentication.
 */
class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up click listeners
        setupClickListeners()
    }
    
    /**
     * Sets up click listeners for authentication options.
     */
    private fun setupClickListeners() {
        // Login button - opens login bottom sheet
        binding.loginButton.setOnClickListener {
            showLoginBottomSheet()
        }
        
        // Sign up button - opens sign up bottom sheet
        binding.signUpButton.setOnClickListener {
            showSignUpBottomSheet()
        }
        
        // Guest mode - navigates directly to main app
        binding.guestModeTextView.setOnClickListener {
            navigateToMain()
        }
    }
    
    /**
     * Shows login bottom sheet dialog.
     */
    private fun showLoginBottomSheet() {
        val loginBottomSheet = LoginBottomSheet()
        loginBottomSheet.show(supportFragmentManager, "login_bottom_sheet")
    }
    
    /**
     * Shows sign up bottom Sheet dialog.
     */
    private fun showSignUpBottomSheet() {
        val signUpBottomSheet = SignUpBottomSheet()
        signUpBottomSheet.show(supportFragmentManager, "sign_up_bottom_sheet")
    }
    
    /**
     * Navigates to main activity.
     */
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        
        // Add transition animation
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
}
