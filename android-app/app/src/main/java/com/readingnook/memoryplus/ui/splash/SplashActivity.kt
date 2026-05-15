package com.readingnook.memoryplus.ui.splash

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.readingnook.memoryplus.databinding.ActivitySplashBinding
import com.readingnook.memoryplus.ui.auth.AuthActivity

/**
 * Splash screen activity for ReadingNook Memory+ app.
 * 
 * Displays app branding with elegant animations and initializes necessary components.
 * Transitions to authentication screen after 2.5 seconds with full-screen immersive mode.
 */
class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable full-screen immersive mode
        enableFullScreenImmersive()
        
        // Initialize view binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Start animations
        startAnimations()
        
        // Navigate to AuthActivity after delay
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAuth()
        }, SPLASH_DELAY)
    }
    
    /**
     * Enables full-screen immersive mode by hiding status bar and navigation bar.
     */
    private fun enableFullScreenImmersive() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        )
    }
    
    /**
     * Starts fade-in and slide-up animations for logo and text.
     */
    private fun startAnimations() {
        // Logo fade-in and slide-up
        val logoFadeIn = ObjectAnimator.ofFloat(binding.logoImageView, View.ALPHA, 0f, 1f)
        logoFadeIn.duration = 800
        logoFadeIn.interpolator = AccelerateDecelerateInterpolator()
        
        val logoSlideUp = ObjectAnimator.ofFloat(binding.logoImageView, View.TRANSLATION_Y, 50f, 0f)
        logoSlideUp.duration = 800
        logoSlideUp.interpolator = AccelerateDecelerateInterpolator()
        
        val logoAnimatorSet = AnimatorSet()
        logoAnimatorSet.playTogether(logoFadeIn, logoSlideUp)
        logoAnimatorSet.startDelay = 200
        logoAnimatorSet.start()
        
        // App name fade-in
        val appNameFadeIn = ObjectAnimator.ofFloat(binding.appNameTextView, View.ALPHA, 0f, 1f)
        appNameFadeIn.duration = 600
        appNameFadeIn.interpolator = AccelerateDecelerateInterpolator()
        appNameFadeIn.startDelay = 400
        appNameFadeIn.start()
        
        // Subtitle fade-in
        val subtitleFadeIn = ObjectAnimator.ofFloat(binding.subtitleTextView, View.ALPHA, 0f, 1f)
        subtitleFadeIn.duration = 600
        subtitleFadeIn.interpolator = AccelerateDecelerateInterpolator()
        subtitleFadeIn.startDelay = 500
        subtitleFadeIn.start()
        
        // Tagline fade-in
        val taglineFadeIn = ObjectAnimator.ofFloat(binding.taglineTextView, View.ALPHA, 0f, 1f)
        taglineFadeIn.duration = 600
        taglineFadeIn.interpolator = AccelerateDecelerateInterpolator()
        taglineFadeIn.startDelay = 600
        taglineFadeIn.start()
        
        // Start bouncing dots animation
        binding.dot1.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, com.readingnook.memoryplus.R.anim.bounce_dot_1))
        binding.dot2.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, com.readingnook.memoryplus.R.anim.bounce_dot_2))
        binding.dot3.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, com.readingnook.memoryplus.R.anim.bounce_dot_3))
    }
    
    /**
     * Navigates to AuthActivity and finishes SplashActivity.
     */
    private fun navigateToAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
        
        // Add transition animation
        overridePendingTransition(
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            enableFullScreenImmersive()
        }
    }
    
    companion object {
        private const val SPLASH_DELAY = 2500L // 2.5 seconds
    }
}
