package com.readingnook.memoryplus.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.readingnook.memoryplus.repository.BookRepository;
import com.readingnook.memoryplus.repository.BookRepository_Impl;
import com.readingnook.memoryplus.repository.NoteRepository;
import com.readingnook.memoryplus.repository.NoteRepository_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile BookRepository _bookRepository;

  private volatile NoteRepository _noteRepository;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `books` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `originalLanguage` TEXT NOT NULL, `translatedLanguage` TEXT NOT NULL, `difficulty` TEXT NOT NULL, `pages` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `pageCount` INTEGER NOT NULL, `isDownloaded` INTEGER NOT NULL, `lastReadPage` INTEGER NOT NULL, `completed` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS `notes` (`id` TEXT NOT NULL, `bookId` TEXT NOT NULL, `selectedWord` TEXT NOT NULL, `originalText` TEXT NOT NULL, `translatedText` TEXT NOT NULL, `hinglishExplanation` TEXT NOT NULL, `difficulty` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `isFavorite` INTEGER NOT NULL, `reviewCount` INTEGER NOT NULL, `lastReviewDate` TEXT NOT NULL, `context` TEXT NOT NULL, `pageNumber` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '93c9e09c475ee3723d4b4acf16e76cd7')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `books`");
        db.execSQL("DROP TABLE IF EXISTS `notes`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsBooks = new HashMap<String, TableInfo.Column>(11);
        _columnsBooks.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("title", new TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("originalLanguage", new TableInfo.Column("originalLanguage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("translatedLanguage", new TableInfo.Column("translatedLanguage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("pages", new TableInfo.Column("pages", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("pageCount", new TableInfo.Column("pageCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("isDownloaded", new TableInfo.Column("isDownloaded", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("lastReadPage", new TableInfo.Column("lastReadPage", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBooks.put("completed", new TableInfo.Column("completed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBooks = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesBooks = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoBooks = new TableInfo("books", _columnsBooks, _foreignKeysBooks, _indicesBooks);
        final TableInfo _existingBooks = TableInfo.read(db, "books");
        if (!_infoBooks.equals(_existingBooks)) {
          return new RoomOpenHelper.ValidationResult(false, "books(com.readingnook.memoryplus.model.Book).\n"
                  + " Expected:\n" + _infoBooks + "\n"
                  + " Found:\n" + _existingBooks);
        }
        final HashMap<String, TableInfo.Column> _columnsNotes = new HashMap<String, TableInfo.Column>(13);
        _columnsNotes.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("bookId", new TableInfo.Column("bookId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("selectedWord", new TableInfo.Column("selectedWord", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("originalText", new TableInfo.Column("originalText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("translatedText", new TableInfo.Column("translatedText", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("hinglishExplanation", new TableInfo.Column("hinglishExplanation", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("difficulty", new TableInfo.Column("difficulty", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("createdAt", new TableInfo.Column("createdAt", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("isFavorite", new TableInfo.Column("isFavorite", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("reviewCount", new TableInfo.Column("reviewCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("lastReviewDate", new TableInfo.Column("lastReviewDate", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("context", new TableInfo.Column("context", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsNotes.put("pageNumber", new TableInfo.Column("pageNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNotes = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNotes = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNotes = new TableInfo("notes", _columnsNotes, _foreignKeysNotes, _indicesNotes);
        final TableInfo _existingNotes = TableInfo.read(db, "notes");
        if (!_infoNotes.equals(_existingNotes)) {
          return new RoomOpenHelper.ValidationResult(false, "notes(com.readingnook.memoryplus.model.Note).\n"
                  + " Expected:\n" + _infoNotes + "\n"
                  + " Found:\n" + _existingNotes);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "93c9e09c475ee3723d4b4acf16e76cd7", "2ba198edcaf5a82779337ab33dad7bb5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "books","notes");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `books`");
      _db.execSQL("DELETE FROM `notes`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(BookRepository.class, BookRepository_Impl.getRequiredConverters());
    _typeConvertersMap.put(NoteRepository.class, NoteRepository_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public BookRepository bookDao() {
    if (_bookRepository != null) {
      return _bookRepository;
    } else {
      synchronized(this) {
        if(_bookRepository == null) {
          _bookRepository = new BookRepository_Impl(this);
        }
        return _bookRepository;
      }
    }
  }

  @Override
  public NoteRepository noteDao() {
    if (_noteRepository != null) {
      return _noteRepository;
    } else {
      synchronized(this) {
        if(_noteRepository == null) {
          _noteRepository = new NoteRepository_Impl(this);
        }
        return _noteRepository;
      }
    }
  }
}
