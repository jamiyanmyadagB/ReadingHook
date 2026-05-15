package com.readingnook.memoryplus

import android.app.backup.BackupAgentHelper
import android.app.backup.FileBackupHelper
import android.app.backup.SharedPreferencesBackupHelper

/**
 * Backup agent for ReadingNook Memory+.
 * 
 * Handles backup of app data including preferences and database files.
 * Ensures user data is preserved across device changes.
 */
class ReadingNookBackupAgent : BackupAgentHelper() {
    
    companion object {
        private const val PREFS_BACKUP_KEY = "shared_prefs"
        private const val DATABASE_BACKUP_KEY = "database"
    }
    
    override fun onCreate() {
        // Backup shared preferences
        addHelper(
            PREFS_BACKUP_KEY,
            SharedPreferencesBackupHelper(this, packageName + "_preferences")
        )
        
        // Backup database files
        addHelper(
            DATABASE_BACKUP_KEY,
            FileBackupHelper(this, "../databases/readingnook_database")
        )
    }
}
