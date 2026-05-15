package com.readingnook.memoryplus.repository;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.readingnook.memoryplus.database.Converters;
import com.readingnook.memoryplus.model.Book;
import com.readingnook.memoryplus.model.Page;
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
public final class BookRepository_Impl implements BookRepository {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Book> __insertionAdapterOfBook;

  private final Converters __converters = new Converters();

  private final SharedSQLiteStatement __preparedStmtOfUpdateReadingProgress;

  private final SharedSQLiteStatement __preparedStmtOfUpdateDownloadStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteBook;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllBooks;

  public BookRepository_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBook = new EntityInsertionAdapter<Book>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `books` (`id`,`title`,`originalLanguage`,`translatedLanguage`,`difficulty`,`pages`,`createdAt`,`pageCount`,`isDownloaded`,`lastReadPage`,`completed`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final Book entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getOriginalLanguage());
        statement.bindString(4, entity.getTranslatedLanguage());
        statement.bindString(5, entity.getDifficulty());
        final String _tmp = __converters.fromPageList(entity.getPages());
        statement.bindString(6, _tmp);
        statement.bindString(7, entity.getCreatedAt());
        statement.bindLong(8, entity.getPageCount());
        final int _tmp_1 = entity.isDownloaded() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getLastReadPage());
        final int _tmp_2 = entity.getCompleted() ? 1 : 0;
        statement.bindLong(11, _tmp_2);
      }
    };
    this.__preparedStmtOfUpdateReadingProgress = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET lastReadPage = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateDownloadStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE books SET isDownloaded = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteBook = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAllBooks = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM books";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final Book book, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfBook.insertAndReturnId(book);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<Book> books, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBook.insert(books);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateReadingProgress(final String bookId, final int lastReadPage,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateReadingProgress.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, lastReadPage);
        _argIndex = 2;
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
          __preparedStmtOfUpdateReadingProgress.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateDownloadStatus(final String bookId, final boolean isDownloaded,
      final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateDownloadStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isDownloaded ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
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
          __preparedStmtOfUpdateDownloadStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteBook(final String bookId, final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteBook.acquire();
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
          __preparedStmtOfDeleteBook.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAllBooks(final Continuation<? super Integer> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllBooks.acquire();
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
          __preparedStmtOfDeleteAllBooks.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<Book>> getAllBooks() {
    final String _sql = "SELECT * FROM books ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
          final int _cursorIndexOfTranslatedLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedLanguage");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfPages = CursorUtil.getColumnIndexOrThrow(_cursor, "pages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpOriginalLanguage;
            _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
            final String _tmpTranslatedLanguage;
            _tmpTranslatedLanguage = _cursor.getString(_cursorIndexOfTranslatedLanguage);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final List<Page> _tmpPages;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPages);
            _tmpPages = __converters.toPageList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final boolean _tmpCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpOriginalLanguage,_tmpTranslatedLanguage,_tmpDifficulty,_tmpPages,_tmpCreatedAt,_tmpPageCount,_tmpIsDownloaded,_tmpLastReadPage,_tmpCompleted);
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
  public Flow<List<Book>> getDownloadedBooks() {
    final String _sql = "SELECT * FROM books WHERE isDownloaded = 1 ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<List<Book>>() {
      @Override
      @NonNull
      public List<Book> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
          final int _cursorIndexOfTranslatedLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedLanguage");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfPages = CursorUtil.getColumnIndexOrThrow(_cursor, "pages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final List<Book> _result = new ArrayList<Book>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final Book _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpOriginalLanguage;
            _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
            final String _tmpTranslatedLanguage;
            _tmpTranslatedLanguage = _cursor.getString(_cursorIndexOfTranslatedLanguage);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final List<Page> _tmpPages;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPages);
            _tmpPages = __converters.toPageList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final boolean _tmpCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp_2 != 0;
            _item = new Book(_tmpId,_tmpTitle,_tmpOriginalLanguage,_tmpTranslatedLanguage,_tmpDifficulty,_tmpPages,_tmpCreatedAt,_tmpPageCount,_tmpIsDownloaded,_tmpLastReadPage,_tmpCompleted);
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
  public Object getBookById(final String bookId, final Continuation<? super Book> $completion) {
    final String _sql = "SELECT * FROM books WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, bookId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Book>() {
      @Override
      @Nullable
      public Book call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfOriginalLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "originalLanguage");
          final int _cursorIndexOfTranslatedLanguage = CursorUtil.getColumnIndexOrThrow(_cursor, "translatedLanguage");
          final int _cursorIndexOfDifficulty = CursorUtil.getColumnIndexOrThrow(_cursor, "difficulty");
          final int _cursorIndexOfPages = CursorUtil.getColumnIndexOrThrow(_cursor, "pages");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfPageCount = CursorUtil.getColumnIndexOrThrow(_cursor, "pageCount");
          final int _cursorIndexOfIsDownloaded = CursorUtil.getColumnIndexOrThrow(_cursor, "isDownloaded");
          final int _cursorIndexOfLastReadPage = CursorUtil.getColumnIndexOrThrow(_cursor, "lastReadPage");
          final int _cursorIndexOfCompleted = CursorUtil.getColumnIndexOrThrow(_cursor, "completed");
          final Book _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final String _tmpOriginalLanguage;
            _tmpOriginalLanguage = _cursor.getString(_cursorIndexOfOriginalLanguage);
            final String _tmpTranslatedLanguage;
            _tmpTranslatedLanguage = _cursor.getString(_cursorIndexOfTranslatedLanguage);
            final String _tmpDifficulty;
            _tmpDifficulty = _cursor.getString(_cursorIndexOfDifficulty);
            final List<Page> _tmpPages;
            final String _tmp;
            _tmp = _cursor.getString(_cursorIndexOfPages);
            _tmpPages = __converters.toPageList(_tmp);
            final String _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getString(_cursorIndexOfCreatedAt);
            final int _tmpPageCount;
            _tmpPageCount = _cursor.getInt(_cursorIndexOfPageCount);
            final boolean _tmpIsDownloaded;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsDownloaded);
            _tmpIsDownloaded = _tmp_1 != 0;
            final int _tmpLastReadPage;
            _tmpLastReadPage = _cursor.getInt(_cursorIndexOfLastReadPage);
            final boolean _tmpCompleted;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfCompleted);
            _tmpCompleted = _tmp_2 != 0;
            _result = new Book(_tmpId,_tmpTitle,_tmpOriginalLanguage,_tmpTranslatedLanguage,_tmpDifficulty,_tmpPages,_tmpCreatedAt,_tmpPageCount,_tmpIsDownloaded,_tmpLastReadPage,_tmpCompleted);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<BookRepository.ReadingStats> getReadingStats() {
    final String _sql = "\n"
            + "        SELECT \n"
            + "            COUNT(*) as totalBooks,\n"
            + "            COUNT(CASE WHEN completed = 1 THEN 1 END) as completedBooks,\n"
            + "            SUM(lastReadPage) as totalPagesRead\n"
            + "        FROM books\n"
            + "        WHERE isDownloaded = 1\n"
            + "    ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"books"}, new Callable<BookRepository.ReadingStats>() {
      @Override
      @NonNull
      public BookRepository.ReadingStats call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfTotalBooks = 0;
          final int _cursorIndexOfCompletedBooks = 1;
          final int _cursorIndexOfTotalPagesRead = 2;
          final BookRepository.ReadingStats _result;
          if (_cursor.moveToFirst()) {
            final int _tmpTotalBooks;
            _tmpTotalBooks = _cursor.getInt(_cursorIndexOfTotalBooks);
            final int _tmpCompletedBooks;
            _tmpCompletedBooks = _cursor.getInt(_cursorIndexOfCompletedBooks);
            final int _tmpTotalPagesRead;
            _tmpTotalPagesRead = _cursor.getInt(_cursorIndexOfTotalPagesRead);
            _result = new BookRepository.ReadingStats(_tmpTotalBooks,_tmpCompletedBooks,_tmpTotalPagesRead);
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
