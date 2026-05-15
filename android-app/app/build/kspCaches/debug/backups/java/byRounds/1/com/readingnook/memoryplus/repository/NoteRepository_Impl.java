package com.readingnook.memoryplus.repository;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.readingnook.memoryplus.model.Note;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class NoteRepository_Impl implements NoteRepository {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Note> __insertionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter<Note> __updateAdapterOfNote;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFavoriteStatus;

  private final SharedSQLiteStatement __preparedStmtOfUpdateReviewInfo;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNote;

  private final SharedSQLiteStatement __preparedStmtOfDeleteNotesByBookId;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllNotes;

  public NoteRepository_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNote = new EntityInsertionAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `notes` (`id`,`bookId`,`selectedWord`,`originalText`,`translatedText`,`hinglishExplanation`,`difficulty`,`createdAt`,`isFavorite`,`reviewCount`,`lastReviewDate`,`context`,`pageNumber`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBookId());
        statement.bindString(3, entity.getSelectedWord());
        statement.bindString(4, entity.getOriginalText());
        statement.bindString(5, entity.getTranslatedText());
        statement.bindString(6, entity.getHinglishExplanation());
        statement.bindString(7, entity.getDifficulty());
        statement.bindString(8, entity.getCreatedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getReviewCount());
        statement.bindString(11, entity.getLastReviewDate());
        statement.bindString(12, entity.getContext());
        statement.bindLong(13, entity.getPageNumber());
      }
    };
    this.__updateAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR REPLACE `notes` SET `id` = ?,`bookId` = ?,`selectedWord` = ?,`originalText` = ?,`translatedText` = ?,`hinglishExplanation` = ?,`difficulty` = ?,`createdAt` = ?,`isFavorite` = ?,`reviewCount` = ?,`lastReviewDate` = ?,`context` = ?,`pageNumber` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Note entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getBookId());
        statement.bindString(3, entity.getSelectedWord());
        statement.bindString(4, entity.getOriginalText());
        statement.bindString(5, entity.getTranslatedText());
        statement.bindString(6, entity.getHinglishExplanation());
        statement.bindString(7, entity.getDifficulty());
        statement.bindString(8, entity.getCreatedAt());
        final int _tmp = entity.isFavorite() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getReviewCount());
        statement.bindString(11, entity.getLastReviewDate());
        statement.bindString(12, entity.getContext());
        statement.bindLong(13, entity.getPageNumber());
        statement.bindString(14, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateFavoriteStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET isFavorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateReviewInfo = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE notes SET lastReviewDate = ?, reviewCount = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNote = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteNotesByBookId = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes WHERE bookId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllNotes = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM notes";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Note note, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfNote.insertAndReturnId(note);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Note> notes, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfNote.insert(notes);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final Note note, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        int _total = 0;
        __db.beginTransaction();
        try {
          _total += __updateAdapterOfNote.handle(note);
          __db.setTransactionSuccessful();
          return _total;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateFavoriteStatus(final String noteId, final boolean isFavorite,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFavoriteStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isFavorite ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, noteId);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateFavoriteStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReviewInfo(final String noteId, final String reviewDate,
      final int reviewCount, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReviewInfo.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, reviewDate);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, reviewCount);
        _argIndex = 3;
        _stmt.bindString(_argIndex, noteId);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateReviewInfo.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNote(final String noteId, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNote.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, noteId);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteNote.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteNotesByBookId(final String bookId,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteNotesByBookId.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, bookId);
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteNotesByBookId.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllNotes(final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllNotes.acquire();
        try {
          __db.beginTransaction();
          try {
            final Integer _result = _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAllNotes.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Note>> getAllNotes() {
    final String _sql = "SELECT * FROM notes ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<Note>>() {
      @Override
      @NonNull
      public List<Note> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSelectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedWord");
          final int _cursorIndexOfOriginalText = CursorUtil.getColumnIndexOrThrow(_cursor, "originalText");
          final int _cursorIndexOfTranslatedText = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedText");
          final int _cursorIndexOfHinglishExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "hinglishExplanation");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfLastReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewDate");
          final int _cursorIndexOfContext = CursorUtil.getColumnIndexOrThrow(_cursor, "context");
          final int _cursorIndexOfPageNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "pageNumber");
          final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Note _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBookId;
            _tmpBookId = _cursor.getString(_cursorIndexOfBookId);
            final String _tmpSelectedWord;
            _tmpSelectedWord = _cursor.getString(_cursorIndexOfSelectedWord);
            final String _tmpOriginalText;
            _tmpOriginalText = _cursor.getString(_cursorIndexOfOriginalText);
            final String _tmpTranslatedText;
            _tmpTranslatedText = _cursor.getString(_cursorIndexOfTranslatedText);
            final String _tmpHinglishExplanation;
            _tmpHinglishExplanation = _cursor.getString(_cursorIndexOfHinglishExplanation);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final String _tmpLastReviewDate;
            _tmpLastReviewDate = _cursor.getString(_cursorIndexOfLastReviewDate);
            final String _tmpContext;
            _tmpContext = _cursor.getString(_cursorIndexOfContext);
            final int _tmpPageNumber;
            _tmpPageNumber = _cursor.getInt(_cursorIndexOfPageNumber);
            _item = new Note(_tmpId,_tmpBookId,_tmpSelectedWord,_tmpOriginalText,_tmpTranslatedText,_tmpHinglishExplanation,_tmpDifficulty,_tmpCreatedAt,_tmpIsFavorite,_tmpReviewCount,_tmpLastReviewDate,_tmpContext,_tmpPageNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Note>> getNotesByBookId(final String bookId) {
    final String _sql = "SELECT * FROM notes WHERE bookId = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, bookId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<Note>>() {
      @Override
      @NonNull
      public List<Note> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSelectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedWord");
          final int _cursorIndexOfOriginalText = CursorUtil.getColumnIndexOrThrow(_cursor, "originalText");
          final int _cursorIndexOfTranslatedText = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedText");
          final int _cursorIndexOfHinglishExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "hinglishExplanation");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfLastReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewDate");
          final int _cursorIndexOfContext = CursorUtil.getColumnIndexOrThrow(_cursor, "context");
          final int _cursorIndexOfPageNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "pageNumber");
          final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Note _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBookId;
            _tmpBookId = _cursor.getString(_cursorIndexOfBookId);
            final String _tmpSelectedWord;
            _tmpSelectedWord = _cursor.getString(_cursorIndexOfSelectedWord);
            final String _tmpOriginalText;
            _tmpOriginalText = _cursor.getString(_cursorIndexOfOriginalText);
            final String _tmpTranslatedText;
            _tmpTranslatedText = _cursor.getString(_cursorIndexOfTranslatedText);
            final String _tmpHinglishExplanation;
            _tmpHinglishExplanation = _cursor.getString(_cursorIndexOfHinglishExplanation);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final String _tmpLastReviewDate;
            _tmpLastReviewDate = _cursor.getString(_cursorIndexOfLastReviewDate);
            final String _tmpContext;
            _tmpContext = _cursor.getString(_cursorIndexOfContext);
            final int _tmpPageNumber;
            _tmpPageNumber = _cursor.getInt(_cursorIndexOfPageNumber);
            _item = new Note(_tmpId,_tmpBookId,_tmpSelectedWord,_tmpOriginalText,_tmpTranslatedText,_tmpHinglishExplanation,_tmpDifficulty,_tmpCreatedAt,_tmpIsFavorite,_tmpReviewCount,_tmpLastReviewDate,_tmpContext,_tmpPageNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Note>> getFavoriteNotes() {
    final String _sql = "SELECT * FROM notes WHERE isFavorite = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<Note>>() {
      @Override
      @NonNull
      public List<Note> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSelectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedWord");
          final int _cursorIndexOfOriginalText = CursorUtil.getColumnIndexOrThrow(_cursor, "originalText");
          final int _cursorIndexOfTranslatedText = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedText");
          final int _cursorIndexOfHinglishExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "hinglishExplanation");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfLastReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewDate");
          final int _cursorIndexOfContext = CursorUtil.getColumnIndexOrThrow(_cursor, "context");
          final int _cursorIndexOfPageNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "pageNumber");
          final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Note _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBookId;
            _tmpBookId = _cursor.getString(_cursorIndexOfBookId);
            final String _tmpSelectedWord;
            _tmpSelectedWord = _cursor.getString(_cursorIndexOfSelectedWord);
            final String _tmpOriginalText;
            _tmpOriginalText = _cursor.getString(_cursorIndexOfOriginalText);
            final String _tmpTranslatedText;
            _tmpTranslatedText = _cursor.getString(_cursorIndexOfTranslatedText);
            final String _tmpHinglishExplanation;
            _tmpHinglishExplanation = _cursor.getString(_cursorIndexOfHinglishExplanation);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final String _tmpLastReviewDate;
            _tmpLastReviewDate = _cursor.getString(_cursorIndexOfLastReviewDate);
            final String _tmpContext;
            _tmpContext = _cursor.getString(_cursorIndexOfContext);
            final int _tmpPageNumber;
            _tmpPageNumber = _cursor.getInt(_cursorIndexOfPageNumber);
            _item = new Note(_tmpId,_tmpBookId,_tmpSelectedWord,_tmpOriginalText,_tmpTranslatedText,_tmpHinglishExplanation,_tmpDifficulty,_tmpCreatedAt,_tmpIsFavorite,_tmpReviewCount,_tmpLastReviewDate,_tmpContext,_tmpPageNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Note>> getNotesNeedingReview() {
    final String _sql = "\n"
            + "        SELECT * FROM notes \n"
            + "        WHERE lastReviewDate = '' \n"
            + "           OR lastReviewDate < datetime('now', '-7 days')\n"
            + "        ORDER BY createdAt DESC\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<Note>>() {
      @Override
      @NonNull
      public List<Note> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSelectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedWord");
          final int _cursorIndexOfOriginalText = CursorUtil.getColumnIndexOrThrow(_cursor, "originalText");
          final int _cursorIndexOfTranslatedText = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedText");
          final int _cursorIndexOfHinglishExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "hinglishExplanation");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfLastReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewDate");
          final int _cursorIndexOfContext = CursorUtil.getColumnIndexOrThrow(_cursor, "context");
          final int _cursorIndexOfPageNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "pageNumber");
          final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Note _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBookId;
            _tmpBookId = _cursor.getString(_cursorIndexOfBookId);
            final String _tmpSelectedWord;
            _tmpSelectedWord = _cursor.getString(_cursorIndexOfSelectedWord);
            final String _tmpOriginalText;
            _tmpOriginalText = _cursor.getString(_cursorIndexOfOriginalText);
            final String _tmpTranslatedText;
            _tmpTranslatedText = _cursor.getString(_cursorIndexOfTranslatedText);
            final String _tmpHinglishExplanation;
            _tmpHinglishExplanation = _cursor.getString(_cursorIndexOfHinglishExplanation);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final String _tmpLastReviewDate;
            _tmpLastReviewDate = _cursor.getString(_cursorIndexOfLastReviewDate);
            final String _tmpContext;
            _tmpContext = _cursor.getString(_cursorIndexOfContext);
            final int _tmpPageNumber;
            _tmpPageNumber = _cursor.getInt(_cursorIndexOfPageNumber);
            _item = new Note(_tmpId,_tmpBookId,_tmpSelectedWord,_tmpOriginalText,_tmpTranslatedText,_tmpHinglishExplanation,_tmpDifficulty,_tmpCreatedAt,_tmpIsFavorite,_tmpReviewCount,_tmpLastReviewDate,_tmpContext,_tmpPageNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<Note>> getNotesByDifficulty(final String difficulty) {
    final String _sql = "SELECT * FROM notes WHERE difficulty = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, difficulty);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<List<Note>>() {
      @Override
      @NonNull
      public List<Note> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfBookId = CursorUtil.getColumnIndexOrThrow(_cursor, "bookId");
          final int _cursorIndexOfSelectedWord = CursorUtil.getColumnIndexOrThrow(_cursor, "selectedWord");
          final int _cursorIndexOfOriginalText = CursorUtil.getColumnIndexOrThrow(_cursor, "originalText");
          final int _cursorIndexOfTranslatedText = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedText");
          final int _cursorIndexOfHinglishExplanation = CursorUtil.getColumnIndexOrThrow(_cursor, "hinglishExplanation");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final int _cursorIndexOfReviewCount = CursorUtil.getColumnIndexOrThrow(_cursor, "reviewCount");
          final int _cursorIndexOfLastReviewDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReviewDate");
          final int _cursorIndexOfContext = CursorUtil.getColumnIndexOrThrow(_cursor, "context");
          final int _cursorIndexOfPageNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "pageNumber");
          final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Note _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpBookId;
            _tmpBookId = _cursor.getString(_cursorIndexOfBookId);
            final String _tmpSelectedWord;
            _tmpSelectedWord = _cursor.getString(_cursorIndexOfSelectedWord);
            final String _tmpOriginalText;
            _tmpOriginalText = _cursor.getString(_cursorIndexOfOriginalText);
            final String _tmpTranslatedText;
            _tmpTranslatedText = _cursor.getString(_cursorIndexOfTranslatedText);
            final String _tmpHinglishExplanation;
            _tmpHinglishExplanation = _cursor.getString(_cursorIndexOfHinglishExplanation);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final boolean _tmpIsFavorite;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp != 0;
            final int _tmpReviewCount;
            _tmpReviewCount = _cursor.getInt(_cursorIndexOfReviewCount);
            final String _tmpLastReviewDate;
            _tmpLastReviewDate = _cursor.getString(_cursorIndexOfLastReviewDate);
            final String _tmpContext;
            _tmpContext = _cursor.getString(_cursorIndexOfContext);
            final int _tmpPageNumber;
            _tmpPageNumber = _cursor.getInt(_cursorIndexOfPageNumber);
            _item = new Note(_tmpId,_tmpBookId,_tmpSelectedWord,_tmpOriginalText,_tmpTranslatedText,_tmpHinglishExplanation,_tmpDifficulty,_tmpCreatedAt,_tmpIsFavorite,_tmpReviewCount,_tmpLastReviewDate,_tmpContext,_tmpPageNumber);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<NoteRepository.NoteStats> getNoteStats() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as totalNotes,\n"
            + "            COUNT(CASE WHEN isFavorite = 1 THEN 1 END) as favoriteNotes,\n"
            + "            COUNT(CASE WHEN difficulty = 'easy' THEN 1 END) as easyNotes,\n"
            + "            COUNT(CASE WHEN difficulty = 'medium' THEN 1 END) as mediumNotes,\n"
            + "            COUNT(CASE WHEN difficulty = 'hard' THEN 1 END) as hardNotes\n"
            + "        FROM notes\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"notes"}, new Callable<NoteRepository.NoteStats>() {
      @Override
      @NonNull
      public NoteRepository.NoteStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalNotes = 0;
          final int _cursorIndexOfFavoriteNotes = 1;
          final int _cursorIndexOfEasyNotes = 2;
          final int _cursorIndexOfMediumNotes = 3;
          final int _cursorIndexOfHardNotes = 4;
          final NoteRepository.NoteStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotalNotes;
            _tmpTotalNotes = _cursor.getInt(_cursorIndexOfTotalNotes);
            final int _tmpFavoriteNotes;
            _tmpFavoriteNotes = _cursor.getInt(_cursorIndexOfFavoriteNotes);
            final int _tmpEasyNotes;
            _tmpEasyNotes = _cursor.getInt(_cursorIndexOfEasyNotes);
            final int _tmpMediumNotes;
            _tmpMediumNotes = _cursor.getInt(_cursorIndexOfMediumNotes);
            final int _tmpHardNotes;
            _tmpHardNotes = _cursor.getInt(_cursorIndexOfHardNotes);
            _result = new NoteRepository.NoteStats(_tmpTotalNotes,_tmpFavoriteNotes,_tmpEasyNotes,_tmpMediumNotes,_tmpHardNotes);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
