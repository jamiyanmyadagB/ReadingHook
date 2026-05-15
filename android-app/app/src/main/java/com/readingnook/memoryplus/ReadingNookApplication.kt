package com.readingnook.memoryplus

import android.app.Application
import com.readingnook.memoryplus.database.AppDatabase
import com.readingnook.memoryplus.repository.BookRepository
import com.readingnook.memoryplus.repository.NoteRepository

/**
 * Application class for ReadingNook Memory+.
 * 
 * Provides singleton access to repository and database instances.
 * Initializes application-level dependencies.
 */
class ReadingNookApplication : Application() {
    
    companion object {
        private lateinit var instance: ReadingNookApplication
        
        fun getInstance(): ReadingNookApplication = instance
    }
    
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }
    
    val bookRepository: BookRepository by lazy {
        database.bookDao()
    }
    
    val noteRepository: NoteRepository by lazy {
        database.noteDao()
    }
    
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
