package com.readingnook.memoryplus.ui.auth

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.readingnook.memoryplus.databinding.ActivityAuthBinding
import com.readingnook.memoryplus.ui.main.MainActivity

/**
 * Authentication activity for ReadingNook Memory+.
 * 
 * Provides login, sign up, and guest mode options with elegant animations.
 * Navigates to main app after authentication.
 */
class AuthActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAuthBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize view binding
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Start entrance animations
        startEntranceAnimations()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    /**
     * Starts fade-in and slide-up animations for UI elements.
     */
    private fun startEntranceAnimations() {
        // Logo fade-in and slide-up
        val logoFadeIn = ObjectAnimator.ofFloat(binding.logoImageView, View.ALPHA, 0f, 1f)
        logoFadeIn.duration = 600
        logoFadeIn.interpolator = AccelerateDecelerateInterpolator()
        
        val logoSlideUp = ObjectAnimator.ofFloat(binding.logoImageView, View.TRANSLATION_Y, 30f, 0f)
        logoSlideUp.duration = 600
        logoSlideUp.interpolator = AccelerateDecelerateInterpolator()
        
        val logoAnimatorSet = android.animation.AnimatorSet()
        logoAnimatorSet.playTogether(logoFadeIn, logoSlideUp)
        logoAnimatorSet.startDelay = 100
        logoAnimatorSet.start()
        
        // App name fade-in
        val appNameFadeIn = ObjectAnimator.ofFloat(binding.appNameTextView, View.ALPHA, 0f, 1f)
        appNameFadeIn.duration = 500
        appNameFadeIn.interpolator = AccelerateDecelerateInterpolator()
        appNameFadeIn.startDelay = 200
        appNameFadeIn.start()
        
        // Subtitle fade-in
        val subtitleFadeIn = ObjectAnimator.ofFloat(binding.subtitleTextView, View.ALPHA, 0f, 1f)
        subtitleFadeIn.duration = 500
        subtitleFadeIn.interpolator = AccelerateDecelerateInterpolator()
        subtitleFadeIn.startDelay = 300
        subtitleFadeIn.start()
        
        // Buttons staggered fade-in
        val loginButtonFadeIn = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f)
        loginButtonFadeIn.duration = 400
        loginButtonFadeIn.interpolator = AccelerateDecelerateInterpolator()
        loginButtonFadeIn.startDelay = 400
        loginButtonFadeIn.start()
        
        val signUpButtonFadeIn = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 0f, 1f)
        signUpButtonFadeIn.duration = 400
        signUpButtonFadeIn.interpolator = AccelerateDecelerateInterpolator()
        signUpButtonFadeIn.startDelay = 500
        signUpButtonFadeIn.start()
        
        val guestButtonFadeIn = ObjectAnimator.ofFloat(binding.guestModeButton, View.ALPHA, 0f, 1f)
        guestButtonFadeIn.duration = 400
        guestButtonFadeIn.interpolator = AccelerateDecelerateInterpolator()
        guestButtonFadeIn.startDelay = 600
        guestButtonFadeIn.start()
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
        binding.guestModeButton.setOnClickListener {
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
     * Shows sign up bottom sheet dialog.
     */
    private fun showSignUpBottomSheet() {
        val signUpBottomSheet = SignUpBottomSheet()
        signUpBottomSheet.show(supportFragmentManager, "sign_up_bottom_sheet")
    }
    
    /**
     * Navigates to main activity.
     */
    private fun navigateToMain() {
        val intent = Intent(this, com.readingnook.memoryplus.ui.main.MainActivity::class.java)
        startActivity(intent)
        finish()
        
        // Add transition animation
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
    
    override fun onBackPressed() {
        super.onBackPressed()
        // Exit app when back is pressed on auth screen
        finishAffinity()
    }
}
