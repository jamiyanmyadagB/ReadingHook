package com.readingnook.memoryplus.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.readingnook.memoryplus.ui.auth.AuthActivity
import com.readingnook.memoryplus.databinding.ActivitySplashBinding

/**
 * Splash screen activity for ReadingNook Memory+ app.
 * 
 * Displays app branding and initializes necessary components.
 * Transitions to authentication screen after delay.
 */
class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Install splash screen
        installSplashScreen()
        
        // Initialize view binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Start transition to main app after delay
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }, SPLASH_DELAY)
    }
    
    companion object {
        private const val SPLASH_DELAY = 2000L // 2 seconds
    }
}
