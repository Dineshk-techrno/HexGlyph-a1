package com.hexglyph.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
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
public final class GlyphDao_Impl implements GlyphDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<GlyphEntity> __insertionAdapterOfGlyphEntity;

  private final EntityDeletionOrUpdateAdapter<GlyphEntity> __deletionAdapterOfGlyphEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public GlyphDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfGlyphEntity = new EntityInsertionAdapter<GlyphEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `glyphs` (`id`,`createdAt`,`direction`,`plaintext`,`groupCodeHint`,`filePath`,`epochDay`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlyphEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getCreatedAt());
        statement.bindString(3, entity.getDirection());
        statement.bindString(4, entity.getPlaintext());
        statement.bindString(5, entity.getGroupCodeHint());
        if (entity.getFilePath() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getFilePath());
        }
        statement.bindLong(7, entity.getEpochDay());
      }
    };
    this.__deletionAdapterOfGlyphEntity = new EntityDeletionOrUpdateAdapter<GlyphEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `glyphs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final GlyphEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM glyphs WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM glyphs";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final GlyphEntity glyph, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfGlyphEntity.insertAndReturnId(glyph);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final GlyphEntity glyph, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfGlyphEntity.handle(glyph);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<GlyphEntity>> observeAll() {
    final String _sql = "SELECT * FROM glyphs ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"glyphs"}, new Callable<List<GlyphEntity>>() {
      @Override
      @NonNull
      public List<GlyphEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPlaintext = CursorUtil.getColumnIndexOrThrow(_cursor, "plaintext");
          final int _cursorIndexOfGroupCodeHint = CursorUtil.getColumnIndexOrThrow(_cursor, "groupCodeHint");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final List<GlyphEntity> _result = new ArrayList<GlyphEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GlyphEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDirection;
            _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            final String _tmpPlaintext;
            _tmpPlaintext = _cursor.getString(_cursorIndexOfPlaintext);
            final String _tmpGroupCodeHint;
            _tmpGroupCodeHint = _cursor.getString(_cursorIndexOfGroupCodeHint);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            _item = new GlyphEntity(_tmpId,_tmpCreatedAt,_tmpDirection,_tmpPlaintext,_tmpGroupCodeHint,_tmpFilePath,_tmpEpochDay);
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
  public Flow<List<GlyphEntity>> observeByDirection(final String dir) {
    final String _sql = "SELECT * FROM glyphs WHERE direction = ? ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, dir);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"glyphs"}, new Callable<List<GlyphEntity>>() {
      @Override
      @NonNull
      public List<GlyphEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPlaintext = CursorUtil.getColumnIndexOrThrow(_cursor, "plaintext");
          final int _cursorIndexOfGroupCodeHint = CursorUtil.getColumnIndexOrThrow(_cursor, "groupCodeHint");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final List<GlyphEntity> _result = new ArrayList<GlyphEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final GlyphEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDirection;
            _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            final String _tmpPlaintext;
            _tmpPlaintext = _cursor.getString(_cursorIndexOfPlaintext);
            final String _tmpGroupCodeHint;
            _tmpGroupCodeHint = _cursor.getString(_cursorIndexOfGroupCodeHint);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            _item = new GlyphEntity(_tmpId,_tmpCreatedAt,_tmpDirection,_tmpPlaintext,_tmpGroupCodeHint,_tmpFilePath,_tmpEpochDay);
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
  public Object getById(final long id, final Continuation<? super GlyphEntity> $completion) {
    final String _sql = "SELECT * FROM glyphs WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<GlyphEntity>() {
      @Override
      @Nullable
      public GlyphEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPlaintext = CursorUtil.getColumnIndexOrThrow(_cursor, "plaintext");
          final int _cursorIndexOfGroupCodeHint = CursorUtil.getColumnIndexOrThrow(_cursor, "groupCodeHint");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfEpochDay = CursorUtil.getColumnIndexOrThrow(_cursor, "epochDay");
          final GlyphEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final String _tmpDirection;
            _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            final String _tmpPlaintext;
            _tmpPlaintext = _cursor.getString(_cursorIndexOfPlaintext);
            final String _tmpGroupCodeHint;
            _tmpGroupCodeHint = _cursor.getString(_cursorIndexOfGroupCodeHint);
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final long _tmpEpochDay;
            _tmpEpochDay = _cursor.getLong(_cursorIndexOfEpochDay);
            _result = new GlyphEntity(_tmpId,_tmpCreatedAt,_tmpDirection,_tmpPlaintext,_tmpGroupCodeHint,_tmpFilePath,_tmpEpochDay);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
