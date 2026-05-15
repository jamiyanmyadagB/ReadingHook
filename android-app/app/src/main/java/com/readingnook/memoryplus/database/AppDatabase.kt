package com.readingnook.memoryplus.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.readingnook.memoryplus.model.Book
import com.readingnook.memoryplus.model.Note
import com.readingnook.memoryplus.repository.BookRepository
import com.readingnook.memoryplus.repository.NoteRepository

/**
 * Room database for ReadingNook Memory+.
 * 
 * Provides database instance and DAO access.
 * Uses singleton pattern for efficient resource management.
 */
@Database(
    entities = [Book::class, Note::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun bookDao(): BookRepository
    abstract fun noteDao(): NoteRepository
    
    companion object {
        private const val DATABASE_NAME = "readingnook_database"
        
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
